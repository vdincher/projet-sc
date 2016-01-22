package etape2;

import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
import java.rmi.registry.*;


public class Irc extends Frame {

	private static final long serialVersionUID = 1L;
	public TextArea		text;
	public TextField	data;
	SharedObject		sentence;
	static String		myName;

	public static void main(String argv[]) {

		if (argv.length != 1) {
			System.out.println("java Irc <name>");
			return;
		}
		myName = argv[0];

		// initialize the system
		Client.init();

		// look up the IRC object in the name server
		// if not found, create it, and register it in the name server
		SharedObject s = Client.lookup("IRC");
		if (s == null) {
			s = Client.create(new Sentence());
			Client.register("IRC", s);
		}
		// create the graphical part
		new Irc(s);
	}

	public Irc(SharedObject s) {

		setLayout(new FlowLayout());

		text=new TextArea(10,60);
		text.setEditable(false);
		text.setForeground(Color.red);
		add(text);

		data=new TextField(60);
		add(data);

		Button write_button = new Button("write");
		write_button.addActionListener(new writeListener(this));
		add(write_button);
		Button read_button = new Button("read");
		read_button.addActionListener(new readListener(this));
		add(read_button);

		setSize(470,300);
		text.setBackground(Color.black); 
		show();

		sentence = s;
	}
}



class readListener implements ActionListener {
	Irc irc;
	public readListener (Irc i) {
		irc = i;
	}
	public void actionPerformed (ActionEvent e) {


		Transaction t = Transaction.getCurrentTransaction();
		if (t==null) {
			t=new Transaction();
		}
		t.start();
		t.add_read(irc.sentence);
		
		try {
			// invoke the method
			String s = ((Sentence)(irc.sentence.getO())).read();

			t.commit();
			
			// display the read value
			irc.text.append(s+"\n");
			System.out.println("Sortie pour read");
		} catch(Exception except) {
			t.abort();
		}

	}
}

class writeListener implements ActionListener {
	Irc irc;
	public writeListener (Irc i) {
		irc = i;
	}
	public void actionPerformed (ActionEvent e) {

		// get the value to be written from the buffer
		String s = irc.data.getText();

		Transaction t = Transaction.getCurrentTransaction();
		if (t==null) {
			System.out.println("a1");
			t=new Transaction();
			System.out.println("a2");
		}
		t.start();
		t.add_write(irc.sentence);

		try {

		// invoke the method
		((Sentence)(irc.sentence.getO())).write(Irc.myName+" wrote "+s);
		irc.data.setText("");
		
		t.commit();

		System.out.println("Sortie du unlock");
		
		} catch(Exception except) {
			t.abort();
		}
	}
}



