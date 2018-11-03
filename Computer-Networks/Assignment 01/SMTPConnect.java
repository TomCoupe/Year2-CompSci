/*************************************
 * Filename:  SMTPConnect.java
 * Names: Daniel Taylor, Thomas Coupe
 * Student-IDs: 201234245, 201241037
 * Date: 11/10/2018
 *************************************/
import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Open an SMTP connection to mailserver and send one mail.
 *
 */
public class SMTPConnect {
    /* The socket to the server */
    private Socket connection;

    /* Streams for reading from and writing to socket */
    private BufferedReader fromServer;
    private DataOutputStream toServer;

    private static final String CRLF = "\r\n";

    /* Are we connected? Used in close() to determine what to do. */
    private boolean isConnected = false;

    /* Create an SMTPConnect object. Create the socket and the
       associated streams. Initialise SMTP connection. */
    public SMTPConnect(EmailMessage mailmessage) throws IOException {
        // Open a TCP client socket with hostname and portnumber specified in
        // mailmessage.DestHost and  mailmessage.DestHostPort, respectively.
		connection = new Socket(mailmessage.DestHost, mailmessage.DestHostPort);

        // attach the BufferedReader fromServer to read from the socket and
        // the DataOutputStream toServer to write to the socket
		fromServer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		toServer = new DataOutputStream(connection.getOutputStream());

		/* Read one line from server and check that the reply code is 220.
		   If not, throw an IOException. */
		String response = fromServer.readLine();
		System.out.println(response);
		if(!response.startsWith("220")) {
			throw new IOException("220 reply not received from server.");
		}

		/* SMTP handshake. We need the name of the local machine.
	       Send the appropriate SMTP handshake command. */
		String localhost = InetAddress.getLocalHost().getHostName();
		sendCommand("HELO "+localhost, 250);
		isConnected = true;
    }

    /* Send message. Write the correct SMTP-commands in the
       correct order. No checking for errors, just throw them to the
       caller. */
    public void send(EmailMessage mailmessage) throws IOException {
		this.sendCommand("MAIL FROM: <" + mailmessage.Sender + ">", 250);
		this.sendCommand("RCPT TO: <" + mailmessage.Recipient + ">", 250);
		this.sendCommand("DATA", 354);
		this.sendCommand(mailmessage.Headers +"\r\n"+ mailmessage.Body + "\r\n.", 250);
	}

    /* Close SMTP connection. First, terminate on SMTP level, then
       close the socket. */
    public void close() {
		isConnected = false;
		try {
			sendCommand("QUIT", 221);
			connection.close();
		} catch (IOException e) {
			System.out.println("Unable to close connection: " + e);
			isConnected = true;
		}
	}

    /* Send an SMTP command to the server. Check that the reply code is
       what is is supposed to be according to RFC 821. */
    private void sendCommand(String command, int rc) throws IOException {

	/* Write command to server and read reply from server. */
		toServer.writeBytes(command + CRLF);
		System.out.println("Command: " + command);
		String response = fromServer.readLine();
		System.out.println(response);

	/* Check that the server's reply code is the same as the parameter
	   rc. If not, throw an IOException. */

		if (!response.startsWith(Integer.toString(rc)))
		{
			throw new IOException(rc + "Reply not received from sever");
		}
	}

    /* Destructor. Closes the connection if something bad happens. */
    protected void finalize() throws Throwable {
		if(isConnected) {
			close();
		}
		super.finalize();
    }
}