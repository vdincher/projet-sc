package etape1;

import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
import java.rmi.registry.*;


public class IRCtest extends Frame {

	private static final long serialVersionUID = 1L;
	public TextArea		text;
	public TextField	data;
	SharedObject		sentence;
	static String		myName;

	public static void main(String argv[]) {

		if (argv.length != 1) {
			System.out.println("java IRCtest <name>");
			return;
		}
		myName = argv[0];

		// initialize the system
		Client.init();

		// look up the IRC object in the name server
		// if not found, create it, and register it in the name server
		SharedObject s = Client.lookup("IRCtest");
		if (s == null) {
			s = Client.create(new Sentence());
			Client.register("IRCtest", s);
		}
		// create the graphical part
		new IRCtest(s);
	}

	public IRCtest(SharedObject s) {

		setLayout(new FlowLayout());

		text=new TextArea(10,60);
		text.setEditable(false);
		text.setForeground(Color.red);
		add(text);

		data=new TextField(60);
		add(data);

		Button write_button = new Button("write");
		write_button.addActionListener(new writeListenerl(this));
		add(write_button);
		Button read_button = new Button("read");
		read_button.addActionListener(new readListenerl(this));
		add(read_button);
		Button lock_read_button = new Button("lock_read");
		lock_read_button.addActionListener(new lockreadListener(this));
		add(lock_read_button);
		Button lock_write_button = new Button("lock_write");
		lock_write_button.addActionListener(new lockwriteListener(this));
		add(lock_write_button);
		Button unlock_button = new Button("unlock");
		unlock_button.addActionListener(new unlockListener(this));
		add(unlock_button);

		setSize(470,300);
		text.setBackground(Color.black); 
		show();

		sentence = s;
	}

}



class lockreadListener implements ActionListener {
	IRCtest irc;
	public lockreadListener (IRCtest i) {
		irc = i;
	}
	public void actionPerformed (ActionEvent e) {

		// lock the object in read mode
		irc.sentence.lock_read();

	}
}

class lockwriteListener implements ActionListener {
	IRCtest irc;
	public lockwriteListener (IRCtest i) {
		irc = i;
	}
	public void actionPerformed (ActionEvent e) {



		// lock the object in write mode

		irc.sentence.lock_write();


	}
}

class writeListenerl implements ActionListener {
	IRCtest irc;

	public writeListenerl (IRCtest i){
		irc=i;
	}
	public void actionPerformed (ActionEvent e) {

		// get the value to be written from the buffer
		String s = irc.data.getText();

		((Sentence)(irc.sentence.getO())).write(Irc.myName+" wrote "+s);
		irc.data.setText("");


	}
}


class unlockListener implements ActionListener {
	IRCtest irc;
	public unlockListener (IRCtest i) {
		irc = i;
	}
	public void actionPerformed (ActionEvent e) {



		// lock the object in write mode

		irc.sentence.unlock();


	}
}


class readListenerl implements ActionListener {
	IRCtest irc;

	public readListenerl (IRCtest i){
		irc=i;
	}
	public void actionPerformed (ActionEvent e) {

		// lock the object in read mode
		String s = ((Sentence)(irc.sentence.getO())).read();
		irc.text.append(s+"\n");
	}


}



