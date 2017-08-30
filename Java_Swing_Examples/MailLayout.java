/**
* Tisha Greenidge
* CSCIE-10b
* HW 4
* 
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*; 


public class MailLayout extends JFrame {

    public static Font f = new Font ("Helvetica", Font.BOLD, 10);

    public static void formatPanels (JPanel panel, JLabel label, JTextField textBox, 
        JPanel container) {
        label.setFont(f);
        panel.setLayout ( new BorderLayout() );
        panel.add (label, BorderLayout.WEST);
        panel.add (textBox, BorderLayout.CENTER);
        panel.setBackground(Color.WHITE);
        container.add (panel);
    }

    public static void formatPanels (JPanel panel, JLabel label, JComboBox<String> comboBox, 
        JPanel container) {
        label.setFont(f);
        panel.setLayout ( new BorderLayout() );
        panel.add (label, BorderLayout.WEST);
        panel.add (comboBox, BorderLayout.CENTER);
        panel.setBackground(Color.WHITE);
        container.add (panel);
    }

    public static void writeToFile (String textMsg) {
        FileOutputStream out = null;

        byte[] msgInBytes = textMsg.getBytes();
        
        try {

            FileOutputStream outputFile = new FileOutputStream("outbox.txt");
            outputFile.write(msgInBytes);
            outputFile.close();

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public MailLayout () {
        String[] emails = {"Tisha Gmail - tisha.m.greenidge@gmail.com", "Tisha Harvard - tig766@harvard.edu", 
                            "Tisha Yahoo - tgreenidge@yahoo.com"};

        setSize(500, 500); 
        setTitle("New Message");
        
        JPanel messageDetails = new JPanel();
        messageDetails.setLayout ( new BoxLayout(messageDetails, BoxLayout.PAGE_AXIS) );

        JTextField toBox = new JTextField ("", 40 );
        JLabel toLabel =  new JLabel ("To:");
        JPanel emailTo = new JPanel();
        formatPanels (emailTo, toLabel, toBox, messageDetails);
        
        JTextField ccBox = new JTextField ("", 40);
        JLabel ccLabel =  new JLabel ("Cc:");
        JPanel emailCc = new JPanel();
        formatPanels (emailCc, ccLabel, ccBox, messageDetails);

        JTextField bccBox = new JTextField ("", 40);
        JLabel bccLabel =  new JLabel ("Bcc:");
        JPanel emailBcc = new JPanel();
        formatPanels (emailBcc, bccLabel, bccBox, messageDetails);
        
        JTextField subjectBox = new JTextField ("", 40);
        JLabel subjectLabel =  new JLabel ("Subject:");
        
        //change title of frame when text is entered
        subjectBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                setTitle(subjectBox.getText());
            }
        });

        JPanel emailSubject = new JPanel();
        formatPanels (emailSubject, subjectLabel, subjectBox, messageDetails);
        
        JComboBox<String> fromBox = new JComboBox<> (emails);
        JLabel fromLabel =  new JLabel ("From:");
        JPanel emailFrom = new JPanel();
        formatPanels (emailFrom, fromLabel, fromBox, messageDetails);
        
        JTextArea emailMessage = new JTextArea (15, 100);

        JButton sendButton = new JButton ("Send");
        
        // On my MAc, button appears white until clicked, then changes to blue
        sendButton.setBackground (Color.BLUE);
        
        //output contents to file
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                String message = emailMessage.getText();
                writeToFile(message);
            }
        });

        sendButton.addActionListener ( new SendMessage() );
        
        sendButton.setFont(f);

        add (sendButton, BorderLayout.NORTH);
        add (messageDetails, BorderLayout.CENTER);
        add (emailMessage, BorderLayout.SOUTH);

        setDefaultCloseOperation (EXIT_ON_CLOSE);
        pack();
        
    }

    public static void main (String [] args) {
        MailLayout emailLayout = new MailLayout();
        emailLayout.setVisible (true);
    }      

}


/**
* When clicked, this button should cause the content of
* the JTextArea to be written to a file named outbox.txt After the message is
* written to file, the screen should be cleared and title should be reset.
*/
class SendMessage implements ActionListener {
         
    public void actionPerformed (ActionEvent e) {
    
        JButton sendButton = (JButton) e.getSource();
        JFrame frame = (JFrame) SwingUtilities.getRoot(sendButton);

        //destroy old screen after save
        frame.setVisible(false);
        frame.dispose();

        //reset screen
        MailLayout emailLayout = new MailLayout();
        emailLayout.setVisible (true);     
        
    }
    
}





