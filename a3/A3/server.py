#!/usr/bin/env python
import sys
import cgi
import MySQLdb as mdb
from mod_python import apache
from time import strftime, gmtime, time
from BaseHTTPServer import HTTPServer, BaseHTTPRequestHandler
from SocketServer import ThreadingMixIn
from threading import Lock

def handler(req):
    """called by the Apache server when an HTTP request is sent. A separate
    process is spawned for each invocation of the script."""

    req.content_type = 'text/plain'
    input = req.read ()
    if input == "":
        return doGET (req)
    else:
        POST = parsePOST (input)
        return doPOST (req, POST)

def splt (datapoint):
    """mapped onto a list of POST parameters to separate the key from the
    values.
    EG: splt ('internal=192.168.0.3') returns ['internal', '192.168.0.3']"""

    return datapoint.split ('=')

def parsePOST (input):
    """takes the parameters of the POST and turns them into a dictionary
    for lookup within doPOST"""

    datapairs = input.split ('&')
    pairs = map (splt, datapairs)
    dict = {}
    for data in pairs:
        dict [data [0]] = data [1]
    return dict

def doGET (req):
    """reads all entries in the mysql database. Deletes entries more than 3
    minutes old. Sends the other entries back to the HTTP getter in the form:
    
    n                       (where n is the number of entries)
    internal1
    external1
    lat1
    long1
    interests1
    internal2
    ...
    longn
    interestsn"""

    users = getFromDB ()

    #first write number of entries
    req.write (str(len(users)) + '\n')
    for lst in users:
        req.write (lst[0]+'\n') #internal
        req.write (lst[1]+'\n') #external
        req.write (lst[2]+'\n') #latitude
        req.write (lst[3]+'\n') #longitude
        req.write (lst[4]+'\n') #interests

    return apache.OK

def doPOST (req, POST):
    """takes POSTs, which always have both IPs, location, and interests,
    but may have two destination IPs and a message as well"""
    valuesMissing = False

    for key in ['internal', 'external', 'latitude', 'longitude', 'interests']:
	#e.g. 'userid: devin'
        try:
            POST[key] #check if it exists
        except:
            valuesMissing = True
    
    #store/update user's entry in the users database
    if not valuesMissing and len(POST['interests']) <= 255:
        storeInDB (POST['internal'], POST['external'], POST['latitude'], 
		POST['longitude'], POST['interests'])
    else:
        return apache.DECLINED

    #now check if they sent a message
    valuesMissing = 0
    for key in ['destInternal', 'destExternal', 'message']:
         try:
             POST[key]
         except:
             valuesMissing += 1

    #three cases (in order): no message, valid message, invalid message
    if valuesMissing == 3:
        getMessages (POST['internal'], POST['external'], req)
        return apache.OK
    elif valuesMissing == 0:
        getMessages (POST['internal'], POST['external'], req)
        return sendMessage (POST) #may fail or succeed
    else:
        return apache.DECLINED

def storeInDB (internal, external, lat, long, interests):
    """either inserts a row into the DB with the data, or updates the latitude,
    longitude, interests, and timestamp."""
    tstamp = str(time())
    connection = mdb.connect('localhost', 'devin', 'admin', 'app')

    with connection:
        cur = connection.cursor ()
        query = "INSERT INTO users VALUES ('{0},{1}','{2}','{3}','{4}','{5}')"
        query += " ON DUPLICATE KEY UPDATE latitude='{2}', longitude='{3}', "
        query += "interests='{4}', TTL='{5}';"
        query = query.format (internal, external, long, lat, interests, tstamp)
        cur.execute (query)

def getFromDB ():
    """create a list of lists. each list is a row from the database.
    if a TTL is more than 3 minutes old, delete it from the database"""

    connection = mdb.connect ('localhost', 'devin', 'admin', 'app')

    currtimefloat = float (time ())
    deletion_time = currtimefloat - 180   #three minutes old
    users = []

    with connection:
        cur = connection.cursor()
        cur.execute ("SELECT * FROM users")
        rows = cur.fetchall ()
        for row in rows:
            timefloat = float(row[4]) #timestamp of entry, represented as float
            if timefloat < deletion_time:
                deleteFromDB (cur, row[0], row [1])
            else:
                rowlst = []
                iptuple = row[0].split(',')
                rowlst.append (iptuple[0])
                rowlst.append (iptuple[1])
                rowlst.append (row[1])
                rowlst.append (row[2])
                rowlst.append (row[3])
                rowlst.append (row[4])
                users.append (rowlst)
    return users

def deleteFromDB (cur, int, ext):
    """deletes the entry from the DB with the given internal/external IP"""
    query = "DELETE FROM users WHERE iptuple='{0},{1}'".format(int, ext)
    cur.execute (query)

def sendMessage (POST):
    """stores a message in the messages database for users to be sent
    when they talk to the server"""
    destInt = POST['destInternal']
    destExt = POST['destExternal']
    message = POST['message']

    #validate message
    if len(message) > 255:
         return apache.DECLINED

    connection = mdb.connect('localhost', 'devin', 'admin', 'app')

    with connection:
        cur = connection.cursor ()
        query = "INSERT INTO messages VALUES ('{0}','{1}','{2}')"
        query = query.format (destInt, destExt, message)
        cur.execute (query)
    return apache.OK

def getMessages (internal, external, req):
    """gets all messages for a user and returns them to client, separated by \n
    """
    connection = mdb.connect ('localhost', 'devin', 'admin', 'app')
    with connection:
        cur = connection.cursor ()
        query = "SELECT internal, external, message FROM messages WHERE "
	query += "internal='{0}' AND external='{1}'"
        query = query.format (internal, external)
        cur.execute (query)

        numberOfMessages = 0
        for row in cur:
            numberOfMessages += 1
        req.write (str(numberOfMessages) + '\n')

        for row in cur:
             req.write (row[0]+'\n') #internal
             req.write (row[1]+'\n') #external
             req.write (row[2]+'\n') #message
             query = "DELETE FROM messages WHERE message='{0}'".format(row[2])
             cur.execute (query)
