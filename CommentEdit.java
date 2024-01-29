import toolbox.file.SearchFolder;
import toolbox.textfile.TextFileHandler;

/**
 * Escreva uma descrição da classe CommentEdit aqui.
 * 
 * @author (seu nome) 
 * @version (um número da versão ou uma data)
 */
public class CommentEdit {
    
    private static int indent = 4;

    private static final String[] BORDER_TOP = {'/' + "*".repeat(79), " ".repeat(indent) + '/' + "*".repeat(79 - indent)};
    private static final String[] BORDER_BOTTOM = {"*".repeat(79) + '/', " ".repeat(indent) + "*".repeat(79 - indent) + '/'};
    
    public static void main(String[] args) throws java.io.IOException {
        
        SearchFolder searchFolder = new SearchFolder(args[0]);
        
        String[] filenames = searchFolder.getPathnamesList(".+\\.java", true);
        
        for (String filename : filenames) {
            
            System.out.println(filename);
            
            TextFileHandler textFileHandler = new TextFileHandler(filename);
            textFileHandler.read();
            
            String className = textFileHandler.getName().replace(".java", "");
            String classRegex = ".*?class\\s+" + className + "(\\s.*)?";
          
            StringBuilder sb = new StringBuilder();
            
            int index = 0; String indentStr = ""; boolean javadoc = false;
            
            while (textFileHandler.hasNext()) {
                
                String line = textFileHandler.nextLine();
                
                if (javadoc) {
                    
                    line = line.trim();
                    if (line.endsWith("*/")) {
                        sb.append(BORDER_BOTTOM[index]);
                        javadoc = false;
                    } 
                    else sb.append(indentStr + line);
                                    
                }
                else {
                    
                    if (line.trim().startsWith("/**")) {
                        javadoc = true;
                        sb.append(BORDER_TOP[index]);
                    }
                    else {
                        if (line.trim().matches(classRegex)) {
                            index = 1; 
                            indentStr = " ".repeat(indent);
                        }
                        sb.append(line);                        
                    }
                    
                }//if-else
                
                sb.append('\n');
                
            }//while

            textFileHandler.setContent(sb.toString());
   
            textFileHandler.write();
            
        }//for
        
    }//main
    
}//classe
