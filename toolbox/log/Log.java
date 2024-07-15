package toolbox.log;

import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.MissingResourceException;


/**
 * A classe fornece metodos estaticos para criar e imprimir em um arquivo de log.
 * 
 * <p>O metodo {@link openFile(String) openFile} cria o arquivo de log se este ainda nao existir, ou abre um arquivo existente para que novos 
 * registros sejam acrescentados. E recomendavel que a chamada para este metodo esteja inserida em um bloco <code>static</code> na classe que
 * contiver o metodo <i><b>main</b></i> de inicializacao do programa.</p>
 * 
 * <p>Assim a chamada para {@link openFile(String) openFile} ira registrar data e hora da inicializacao do programa.</p>
 * 
 * <p>Da mesma forma, o metodo {@link closeFile(String) closeFile} deve ser chamado como uma das ultimas instrucoes executadas pelo
 * programa. Registrando no arquivo de log a data e hora do termino do programa.</p>
 * 
 * <p>Com o arquivo de log aberto, os metodos estaticos para imprimir mensagens e dados no log (fornecidos por esta classe) podem ser 
 * utilizados para registrar dados e informacoes sobre a execucao do programa. Porem se um destes metodos for chamado sem que o arquivo
 * de log exista ou esteja aberto, esta chamada sera sem efeito, nao retornando mensagem de erro ou lancando alguma excecao.</p>
 * 
 * <p>Tambem se ocorrer alguma falha ao tentar imprimir uma mensagem no arquivo de log, nenhuma excecao sera lançada, apenas tal mensagem
 * nao sera gravada no arquivo de log.</p>
 * 
 * <p>Nao e necessario fechar o arquivo para o flush dos dados no buffer de saida. A cada saida para o arquivo, um flush de dados e realizado
 * automaticamente, evitando assim que haja perda de informacao caso o programa termine abruptamente</p>
 * 
 * @author Pedro Reis
 * 
 * @since 1.0 - 29 de junho de 2024
 * 
 * @version 1.0 
 */
public final class Log {
    
    private static PrintStream logStream;
    
    private static String separator;
     
    private static Calendar rightNow;
    
    private static boolean indentActive;
    
    private static String indentString;
  
    private static LinkedList<String> stack;
    
    private static String msg$1;
    
    static {
        
        try {
            
            ResourceBundle rb = 
                ResourceBundle.getBundle("toolbox.properties.Log", toolbox.locale.Localization.getLocale());
            msg$1 = rb.getString("msg$1");
 
        
        } catch (NullPointerException | MissingResourceException | ClassCastException e) {
            
            msg$1 = "Unmatched method return in log file";
  
        }         
        
        logStream = null;
        separator = toolbox.string.StringTools.repeatChar('*', 80);
        rightNow = Calendar.getInstance();
        indentActive = true;
        indentString = "";
        stack = new LinkedList<>();
        
    }
    
    /**
     * Retorna se ha um arquivo de log configurado e aberto.
     * 
     * @return <code>true</code> se existir um arquivo de log aberto para saida.
     */
    public static boolean isSet() {
        
        return logStream != null;
        
    }//isSet
    
    /*
     * As saidas para o arquivo de log sao realizadas por este metodo.
     */
    private static void printf(final String format, final String msg) {
        
        if (isSet()) {
           
            if (indentActive) logStream.print(indentString);
   
            logStream.printf(format, msg);
            
            indentActive = (format.length() == 4); // format == "%s%n" imprimiu com quebra de linha
        }
        
    }//printf
    
    /**
     * Imprime uma mensagem no arquivo de log sem quebrar linha.
     * 
     * @param msg A mensagem.
     */
    public static void print(final String msg) {        
        
        printf("%s", msg);        
        
    }//print    
    
    /**
     * Imprime uma mensagem com quebra de linha e deixa uma linha em branco apos esta mensagem. Ou seja, quebra duas linhas apos imprimir a 
     * mensagem.
     * 
     * @param msg A mensagem.
     */
    public static void println(final String msg) {        
        
        printf("%s%n", msg);
        
    }//println
    
    /**
     * Imprime a data passada no formato dd/mm/aaaa.
     * 
     * @param date A data-hora.
     */
    public static void printDate(final Date date) {
        
        print(String.format("%1$td/%1$tm/%1$tY", date));
        
    }//printDate
    
    /**
     * Imprime a hora passada no formato hh:mm:ss:ms.
     * 
     * @param date A data-hora.
     */
    public static void printTime(final Date date) {
        
        print(String.format("%1$tHh:%1$tMm:%1$tSs:%1$tLms", date));
        
    }//printTime
    
    /**
     * Imprime a data e a hora no momento em que este metodo foi chamado no formato dd/mm/aaaa hh:mm:ss:ms.
     */
    public static void printDateTime() {
        
        Date now = rightNow.getTime();
        printDate(now); print(" "); printTime(now);
        
    }//printDateTime
    
    /*
     * 
     */
    private static void printStart() {
        
        println(separator);
        printDateTime(); println(""); 
        
    }//printStart
    
    /*
     * 
     */
    private static void printFinish() {

        printDateTime(); println("");
        
    }//printFinish  
    
    /**
     * Pode ser chamado no inicio da execucao de um metodo.
     * 
     * @param pack O pacote da classe que contem o metodo.
     * @param clas A classe do metodo.
     * @param meth O nome do metodo.
     */
    public static void exec(final String pack, final String clas, final String meth) {
        
        String id = pack + '.' + clas + ": " + meth;
        stack.push(id);
        println("--> " + id);
        indentString += "|\t";
        
    }//exec
    
    /**
     * Pode ser chamado imediatamente antes do encerramento da execucao de um metodo. 
     * 
     * <p>Para que esse metodo possa ser executado corretamente, e necessario que o metodo {@link exec(String, String, String) exec} tenha sido
     * chamado previamente durante a execucao deste mesmo metodo.</p>
     * 
     * <p>Se o metodo onde ret e chamado declara alguma excecao, entao a chamada de ret deve ser inserida em um bloco <code>finally</code>
     * associado a um bloco <code>try</code> englobando o escopo de codigo do metodo.</p>
     * 
     * @param pack O pacote da classe que contem o metodo. Deve ser identico a valor passado no argumento pack do metodo exec correspondente.
     * @param clas A classe do metodo. Deve ser identico a valor passado no argumento clas do metodo exec correspondente.
     * @param meth O nome do metodo.Deve ser identico a valor passado no argumento meth do metodo exec correspondente.
     * 
     * @throws IllegalStateException Caso a chamada deste metodo nao corresponda a ultima chamada de um metodo 
     * {@link exec(String, String, String) exec}, ou seja, ao metodo exec que deve ter sido chamado no corpo deste mesmo metodo. 
     * 
     */
    public static void ret(final String pack, final String clas, final String meth) throws IllegalStateException {
        
        String thisId = pack + '.' + clas + ": " + meth;        
        
        if (stack.isEmpty()) throw new IllegalStateException(msg$1);
        
        String id = stack.pop();
        
        if (!id.equals(thisId)) throw new IllegalStateException(msg$1);
        
        indentString = indentString.substring(0, indentString.length() - 2);
        println("<-- " + id);

    }//ret 
    
    /**
     * Pode ser chamado imediatamente antes do encerramento da execucao de um metodo. 
     * 
     * <p>Para que esse metodo possa ser executado corretamente, e necessario que o metodo {@link exec(String, String, String) exec} tenha sido
     * chamado previamente durante a execucao deste mesmo metodo.</p>
     * 
     * @param pack O pacote da classe que contem o metodo. Deve ser identico a valor passado no argumento pack do metodo exec correspondente.
     * @param clas A classe do metodo. Deve ser identico a valor passado no argumento clas do metodo exec correspondente.
     * @param meth O nome do metodo.Deve ser identico a valor passado no argumento meth do metodo exec correspondente.
     * 
     * @param returnValue Deve ser o valor que sera retornado por este metodo. Este valor sera registrado no arquivo de log.
     * 
     * @throws IllegalStateException Caso a chamada deste metodo nao corresponda a ultima chamada de um metodo 
     * {@link exec(String, String, String) exec}, ou seja, ao metodo exec que deve ter sido chamado no corpo deste mesmo metodo. 
     * 
     */
    public static void ret(final String pack, final String clas, final String meth, final Object returnValue) throws IllegalStateException {

        String s = 
            returnValue.toString().replace(toolbox.string.StringTools.NEWLINE, toolbox.string.StringTools.NEWLINE + indentString);
        println("@return : " + s);
        
        ret(pack, clas, meth);

    }//ret      
    
    /**
     * Imprime no arquivo de log os valores dos parametros passados ao metodo.
     * 
     * <p>Exempo de uso:</p>
     * 
     * <pre>
     * <code>
     *  
        public static String method(int x, int y) {
            Log.exec("toolbox.log", "Test", "method");
            Log.param(x, y);
            Log.println("method executando");  
            String s = "Essa String sera retornada.";
            Log.ret("toolbox.log", "Test", "method", s);
            return s;
        }//method
        
        </code>
        </pre>
        
        <p>Abaixo, um exemplo de como seria a saida da execucao deste metodo no arquivo de log, se os valores passados para os parametros 
        x e y fosse, respectivamente, 1 e 2.
        
        <pre>
        <code>
        
    --> toolbox.log.Test: method
    |    @param1 : 1
    |    @param2 : 2
    |    method executando
    |    @return : Essa String sera retornada.
    |    retorno
    <-- toolbox.log.Test: method
    
      </pre>
      </code>    
      
     * 
     * 
     */
    public static void param(Object... params) {
        
        int n = 0;
        
        for (Object param : params) {
            
            String s = 
                param.toString().replace(toolbox.string.StringTools.NEWLINE, toolbox.string.StringTools.NEWLINE + indentString);
            println("@param" + (++n) + " : " + s);
        }
        
    }//param    
    
    
    /**
     * Fecha o arquivo de log e imprime a data e hora corrente.
     */
    public static void closeFile() {
        
        if (!isSet()) return;
        
        printFinish();
        
        logStream.close();
        
        logStream = null;
        
    }//closeFile
    
    /**
     * Abre e indica qual arquivo sera usado para as saidas de log. 
     * 
     * <p>Se o arquivo nao existir, sera criado. Se existir, sera aberto com os novos registros sendo acrescentados aos ja existentes.</p>
     * 
     * <p>Quando este metodo for chamado, sera gravado  um registro com a data e hora correntes no arquivo de log</p>
     * 
     * <p>Por padrao, os registros serao gravados com o enconding UTF8. Se o sistema nao suportar UTF8, sera usado o enconding padrao do sistema.</p>
     * 
     * <p>Este metodo deve ser chamado de um bloco de inicializacao <code>static</code> na classe principal do programa. Antes da
     * execucao do metodo main.</p>
     * 
     * @param pathname O pathname do arquivo de log.
     * 
     * @throws FileNotFoundException se o arquivo nao puder ser criado ou aberto para escrita.
     */
    public static void openFile(final String pathname) throws java.io.FileNotFoundException {
        
        try {            
        
            logStream = new PrintStream(new FileOutputStream(pathname, true), true, "utf8");
        }
        catch(UnsupportedEncodingException e) {
            
            logStream = new PrintStream(new FileOutputStream(pathname, true), true);
        };
        
        printStart();      
    
    }//openFile    

}//classe Log
