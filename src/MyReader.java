
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import static java.lang.Thread.sleep;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;



/**
 *
 * @author mark harrington
 */
public class MyReader implements Runnable{

    
    private final String filename  ; // this holds file name 
    private Reader fileReader;   // file  reader reads the file in 
    private List<String> lines ;
    private int paragraphcounter =0 ;
    int charcounter = 0 ; 
    private Map<Integer, String> pmap ; 
    public String  m_string = "";
    String message ;
    
    //Colours for console applications 
    
    private final String m_CYAN="\033[1;36m" ;    
    private final String m_GREEN ="\033[1;32m";
    private final String m_RESET ="\033[0m" ;
    private final String m_RED ="\033[0;31m" ;
    private final String m_BLUE= "\033[0;34m" ;
    private final String m_PURPLE="\033[1;35m" ;
    private final String m_YELLOW="\033[0;33m";
    private final String m_WHITE="\033[1;37m";
    private  int index = 1 ;
    
    // Array to hold this colors 
    
    String [] Carray = {"" , m_GREEN,m_RED,m_BLUE,m_YELLOW,m_WHITE};
    
    private volatile boolean running;
    
    //  I  pass the file object to the  constructor 
    public MyReader(String f)
    {
        running = true;
        // contructor  will take a  file as parammeter and read this in 
        
        this.filename = f ; 
        readFile(filename);
        this.getmapEntry(1);
    }
  
    
    // the run method of the  class
    // called via main 
    
    
    
    @Override
   public  void run(){ 
        //  run method 
        System.out.println("Run method called");
        
        
        
        while(running)
         {
            try {
                
                if(charcounter < message.length()){
                System.out.print(message.charAt(charcounter));
                }
                sleep(70);
                charcounter ++ ; 
                
            } catch (InterruptedException ex) {
                
            }
             
            if(charcounter >=message.length())
            {
                running= false ;
                charcounter =0 ;
            }
         }
         
         
         
        
        
             
       
   }
  
   
   public void showMenu()
   {
      System.out.println("Select Choice           \n"
                         +"Enter 1 to start..     \n"
                         +"Enter 2 Next paragaph  \n"
                         +"Enter 3 To stop Thread \n"
                         +"Enter 4 To quit        \n\n");
                             
       
   }
   
   
    // get mesaage for self checks 
   
    public String getMessage(){   
        return message ; 
    }
  
   
   // stop the thread  gracefully 
    
   public void threadstopStart(boolean f)
           
   {
       charcounter = 0  ;   // reset 
       running = f ; 
       
   }
    
   public boolean checkrunstate()
   {
       return running;
       
   }
           
    
   // method to  set message to map entry  for paragraphs 
   public void  getmapEntry(int entry)
   {       
       int iCol = getNumberOfParagrapghs()+1; 
       index = entry ;
       message = (String)pmap.get(entry);
       if(index < Carray.length){
       message = Carray[index]+message+m_RESET ;
       }
       else{ 
       index=1;
       }
       
       
       
       
   }
      
   
    // to return the number of entries 
    public int getNumberOfParagrapghs()
    {
        return pmap.size();
    }
    
   // read the file in and convert to List<Strings> 
    
   public final void readFile(String filename)
   {
       
       String line ;
       
        try {
          
        fileReader = new FileReader(filename);
        
            
       
           try ( // buffered read is what we stor the file contents in
           // file reader is  the class that reads the file object
               BufferedReader bufferedReader = new BufferedReader(fileReader)) {
               pmap= new HashMap<>();
               
               while ((line = bufferedReader.readLine()) != null)
               {
                   
                   // to check fro new paragrapghs  is use a simple html tag <P>
                   if(!(line.startsWith("<P>")))
                   {
                       
                       // if its a sentance then I stor this 
                          m_string+=line +"\n" ;
                       
                   }else
                        {

                            // it not so its blank line 
                            paragraphcounter ++ ; // incremment paragraphs 
                            // place this into hashmap and store the entier 
                            // colletion of lines as a paragraph 
                            pmap.put(paragraphcounter, m_string);
                            // reset string to hold nothing and loop
                            // untill  eof  
                             m_string ="" ;

                        }
                   
               }
           }
        } catch (IOException ex) {            
        
        
        }
         
     //  just self check for my self           
     //System.out.println(pmap.size());
          
   }
    
    public static void main(String args[]) throws InterruptedException{
        
        // the location of the file im reading 
        // this will change 
        
        String m_file= "/home/mark/NetBeansProjects/helloworld/mytext.txt";
        
        
        // this for prompting for  next paragraph to display 
        int selection = 1 ;
        String inchar = "" ; // getting input from keyboard
        int m_choice =0;
        int max_paragraphs;
        // create and instance of the class   and pass  file location to 
        //constructor
        
        
        MyReader m_reader = new MyReader(m_file) ;
           
        // get the maxumum number of praragraphs 
        
        max_paragraphs= m_reader.getNumberOfParagrapghs() ;
        
              
       
       System.out.println("Enter choice");
       Scanner sc = new Scanner(System.in)  ;
      
    
       
    
       
       while(m_choice < 1||m_choice >4)
       {
          
           m_reader.showMenu();
           m_choice =sc.nextInt();
           
           switch(m_choice){
            
               case 1:                       
                        m_reader.threadstopStart(true);
                        
                        
                        Thread t = new Thread(m_reader);
                        t.start();
                        t.join();
                        
                        
                       
                        
                        break ;
               case 2 : 
                        m_reader.threadstopStart(true);
                        selection+=1 ;
                        
                        if(selection< (max_paragraphs+1)){
                        m_reader.getmapEntry(selection) ; 
                        
                        }  
                        Thread t2 = new Thread(m_reader);
                        t2.start();  
                        
                        t2.join();
                        
                       
                        break ;
                    
               case 3 : m_reader.threadstopStart(false);
                        System.out.println("Stopping Thread\n\n");
                        break ;
                        
                        
               case 4 :
                        
                        m_reader.threadstopStart(false);
                        System.out.println("Thankyou for using  Now Exiting application\n\n");
                        System.exit(0);
                        
                        break ; 
                   
                   
                       
                   
                   
           }
           if(selection >= max_paragraphs)
           {
              selection = 0 ; // reset this 
           }
           m_choice=0 ;  // reset m choice 
       }
       
            
       }
}// end class
