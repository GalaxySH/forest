package forest1;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class FancyConsole extends JFrame 
{

  int guiWidth;
  int guiHeight;
	JTextField tfIn;
  JTextArea taOut;
  JScrollPane scrollPane;
  String consoleText;
  JLabel consoleImage;
	

	private final PipedInputStream inPipe = new PipedInputStream(); 
  private final PipedInputStream outPipe = new PipedInputStream(); 

	PrintWriter inWriter;
	
	
	FancyConsole(String title, int width, int height) 
  {
		super(title);
    guiWidth = width;
    guiHeight = height;

	 
	    // 2. set the System.in and System.out streams 
	    System.setIn(inPipe); 
	    try 
      {
	    	System.setOut(new PrintStream(new PipedOutputStream(outPipe), true)); 
	    	inWriter = new PrintWriter(new PipedOutputStream(inPipe), true); 
	    }
	    catch(IOException e) 
      {
	    	System.out.println("Error: " + e);
	    	return;
	    }

      consoleText = "";
	    
	    JPanel panel = new JPanel(new BorderLayout());
      taOut = new JTextArea(20, 40);
      scrollPane = new JScrollPane(taOut);
      panel.add(scrollPane, BorderLayout.CENTER);

      consoleImage = new JLabel();

      panel.add(consoleImage, BorderLayout.NORTH);
	    
	    add(panel);

    // to get the correct InputMap
      int condition = JTextArea.WHEN_FOCUSED;  
      // get our maps for binding from the chatEnterArea JTextArea
      InputMap inputMap = taOut.getInputMap(condition);
      ActionMap actionMap = taOut.getActionMap();

      // the key stroke we want to capture
      KeyStroke enterStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);

      // tell input map that we are handling the enter key
      inputMap.put(enterStroke, enterStroke.toString());

      // tell action map just how we want to handle the enter key
      actionMap.put(enterStroke.toString(), new AbstractAction() 
      {

         @Override
         public void actionPerformed(ActionEvent arg0) 
         {
           // handle input (on enter key)
            String text = taOut.getText();
            String inputText;
            int currLength = text.length();
            int oldLength = consoleText.length();
            if (currLength > oldLength)
            {
                inputText = text.substring(oldLength, currLength);
            }
            else
            {
                inputText = null;
            }
            
            consoleText = text;

            inWriter.println(inputText); 
         }
      });

    CaretListener caretListen = new CaretListener() 
    {
      public void caretUpdate(CaretEvent caretEvent) 
      {
        int caretPos = caretEvent.getDot();
        if (caretPos < consoleText.length())
        {
          // prevent cursor from going into console output
          taOut.setCaretPosition(consoleText.length());
        }
      }
    };
	  
    taOut.addCaretListener(caretListen);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		setSize(guiWidth, guiHeight);
		
	    new SwingWorker<Void, String>() 
      { 
	         protected Void doInBackground() throws Exception 
           { 
	            Scanner s = new Scanner(outPipe);
	            while (s.hasNextLine()) 
              {
	            		 String line = s.nextLine();
		            	 publish(line);
	            }
              s.close();
	            return null; 
	        } 
	         @Override protected void process(java.util.List<String> chunks) 
           { 
             // Handle output
               for (String line : chunks)
               {
                  taOut.append("\n" + line);
               }

               consoleText = taOut.getText();
	         } 

	    }.execute(); 

	}

  public void setImage(String newImage)
  {
      ImageIcon imageIcon = new ImageIcon(this.getClass().getResource(newImage));
      Image img = imageIcon.getImage();
      Image scaledImg = img.getScaledInstance(guiWidth,-1,java.awt.Image.SCALE_SMOOTH);
      ImageIcon scaledIcon = new ImageIcon(scaledImg);

      consoleImage.setIcon(scaledIcon);
  }
}
