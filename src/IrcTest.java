

import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import java.io.*;
import java.net.*;
import java.util.*;

import java.lang.*;
import java.rmi.registry.*;


public class IrcTest extends Frame {

	private static final long serialVersionUID = 1L;
	public TextArea		text;
	public TextField	data;
	Sentence_itf		sentence;
	static String		myName;
	static boolean		useTransaction;
	
	Button write_button = new Button("write");
	Button read_button = new Button("read");
	Button lock_read_button = new Button("lock_read");
	Button lock_write_button = new Button("lock_write");
	Button unlock_button = new Button("unlock");

	public static void main(String argv[]) {
		
		if ((argv.length != 1 && argv.length != 2) || (argv.length == 2 && !argv[1].equals("-t"))) {
			System.out.println("java Irc <name> [-t]");
			return;
		}
		myName = argv[0];
		if (argv.length == 2) {
			useTransaction = true;
		} else {
			useTransaction = false;
		}

		// initialize the system
		Client.init();

		// look up the IRC object in the name server
		// if not found, create it, and register it in the name server
		Sentence_itf s = (Sentence_itf) Client.lookup("IRC");
		if (s == null) {
			s = (Sentence_itf) Client.create(new Sentence());
			Client.register("IRC", s);
		}
		// create the graphical part
		new IrcTest(s);
	}

	public IrcTest(Sentence_itf s) {

		setLayout(new FlowLayout());

		text=new TextArea(10,60);
		text.setEditable(false);
		text.setForeground(Color.red);
		add(text);

		data=new TextField(60);
		add(data);

		
		write_button.addActionListener(new writeListener2(this));
		add(write_button);
		
		read_button.addActionListener(new readListener2(this));
		add(read_button);
		
		lock_read_button.addActionListener(new lockreadListener2(this));
		add(lock_read_button);
		
		lock_write_button.addActionListener(new lockwriteListener2(this));
		add(lock_write_button);
		
		unlock_button.addActionListener(new unlockListener2(this));
		add(unlock_button);
		
		lock_read_button.setEnabled(true);
		lock_write_button.setEnabled(true);
		read_button.setEnabled(false);
		write_button.setEnabled(false);
		unlock_button.setEnabled(false);

		

		setSize(470,300);
		text.setBackground(Color.black); 
		show();

		sentence = s;
	}

}



class lockreadListener2 implements ActionListener {
	IrcTest irc;
	public lockreadListener2 (IrcTest i) {
		irc = i;
	}
	public void actionPerformed (ActionEvent e) {
		


		// lock the object in read mode
		irc.sentence.lock_read();
		irc.lock_read_button.setEnabled(false);
		irc.lock_write_button.setEnabled(false);
		irc.read_button.setEnabled(true);
		irc.write_button.setEnabled(false);
		irc.unlock_button.setEnabled(true);

	}
}

class lockwriteListener2 implements ActionListener {
	IrcTest irc;
	public lockwriteListener2 (IrcTest i) {
		irc = i;
	}
	public void actionPerformed (ActionEvent e) {



		// lock the object in write mode

		irc.sentence.lock_write();
		irc.lock_read_button.setEnabled(false);
		irc.lock_write_button.setEnabled(false);
		irc.read_button.setEnabled(false);
		irc.write_button.setEnabled(true);
		irc.unlock_button.setEnabled(true);


	}
}

class writeListener2 implements ActionListener {
	IrcTest irc;

	public writeListener2 (IrcTest i){
		irc=i;
	}
	public void actionPerformed (ActionEvent e) {

		// get the value to be written from the buffer
		String s = irc.data.getText();

		if (irc.useTransaction) {

			Transaction t = new Transaction();
			t.start();

			try {

				// invoke the method
				irc.sentence.write(Irc.myName+" wrote "+s);
				irc.data.setText("");

				t.commit();

				System.out.println("Sortie du unlock");

			} catch(Exception except) {
				t.abort();
			}
		} else {

			// invoke the method
			irc.sentence.write(Irc.myName+" wrote "+s);
			irc.data.setText("");

		}
		

		irc.lock_read_button.setEnabled(false);
		irc.lock_write_button.setEnabled(false);
		irc.read_button.setEnabled(false);
		irc.write_button.setEnabled(true);
		irc.unlock_button.setEnabled(true);


	}
}


class unlockListener2 implements ActionListener {
	IrcTest irc;
	public unlockListener2 (IrcTest i) {
		irc = i;
	}
	public void actionPerformed (ActionEvent e) {



		// lock the object in write mode

		irc.sentence.unlock();
		irc.lock_read_button.setEnabled(true);
		irc.lock_write_button.setEnabled(true);
		irc.read_button.setEnabled(false);
		irc.write_button.setEnabled(false);
		irc.unlock_button.setEnabled(false);


	}
}


class readListener2 implements ActionListener {
	IrcTest irc;

	public readListener2 (IrcTest i){
		irc=i;
	}
	public void actionPerformed (ActionEvent e) {

		if (irc.useTransaction) {

			Transaction t = new Transaction();
			t.start();

			try {
				// invoke the method
				String s = irc.sentence.read();

				t.commit();

				// display the read value
				irc.text.append(s+"\n");
				System.out.println("Sortie pour read");
			} catch(Exception except) {
				t.abort();
			}
		} else {

			// invoke the method
			String s = irc.sentence.read();

			// display the read value
			irc.text.append(s+"\n");
		}
		
		irc.lock_read_button.setEnabled(false);
		irc.lock_write_button.setEnabled(false);
		irc.read_button.setEnabled(true);
		irc.write_button.setEnabled(false);
		irc.unlock_button.setEnabled(true);
	}


}



