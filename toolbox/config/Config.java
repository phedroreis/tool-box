package toolbox.config;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;

/**
 * Uma classe para manipular arquivos de configuraçao.
 * 
 * @author Pedro Reis 
 * @since 1.0 - 6 de julho de 2024
 * @version 1.0
 */
public final class Config {
    
    private final String pathname;
    
    private toolbox.textfile.TextFileHandler textFileHandler;
    
    public Map<String, String> map;
    
    /**
     * Construtor da classe.
     * 
     * <p>Cria o arquivo de configuraçao com charset UTF8. Se este charset nao for suportado pelo sistema, cria
     * o arquivo com o charset default.</p>
     * 
     * @param pathname O pathname do arquivo de configuracao.
     */
    public Config(final String pathname) {
        
        this.pathname = pathname;
        
        try {
            
            textFileHandler = new toolbox.textfile.TextFileHandler(pathname, "UTF8");
        }
        catch(IllegalCharsetNameException | UnsupportedCharsetException e) {
            
            textFileHandler = new toolbox.textfile.TextFileHandler(pathname);
        }  
        
        map = new HashMap<>();
        
    }//construtor
    
    /**
     * Le o arquivo de configuraçao.
     * 
     * @throws IOException Em caso de erro de IO.
     */
    public void read() throws IOException {

        textFileHandler.read();

        String fileContent = textFileHandler.getContent();
        
        Scanner scanner = new Scanner(fileContent);
        
        Pattern pattern = Pattern.compile("(.+?) = (.+)");
       
        while (scanner.hasNext()) {
             
            Matcher matcher = pattern.matcher(scanner.nextLine());
            
            if (matcher.find()) map.put(matcher.group(1), matcher.group(2));
            
        }
         
    }//read
    
    /**
     * Atualiza o arquivo, sobrescrevendo a versao anterior.
     * 
     * @throws IOException Em caso de erro de IO.
     */
    public void write() throws IOException {
        
        StringBuffer stringBuffer = new StringBuffer();
        
        Set<String> set = map.keySet();
        
        for (String key : set) stringBuffer.append(key).append(" = ").append(map.get(key)).append(toolbox.string.StringTools.NEWLINE);
            
        textFileHandler.setContent(stringBuffer.toString());
        
        textFileHandler.write();        
        
    }//write
    
    /**
     * Retorna se o arquivo de configuraçao contem um mapeamento para esta chave.
     * 
     * @param key A chave.
     * 
     * @return <code>true</code> se existe  um mapeamento para a chave.
     */
    public boolean containsKey(final String key) {
        
        return map.containsKey(key);
        
    }//containsKey
    
    /**
     * Escreve ou atualiza um par chave/valor no arquivo de configuraçao.
     * 
     * @param key A chave.
     * @param value O valor da chave.
     */
    public void put(final String key, final String value) {
        
        map.put(key, value);
        
    }//put
    
    /**
     * Retorna o valor para a chave passada no argumento. Ou <code>null</code> se nao existir este mapeamento.
     * 
     * @return O valor da chave, ou <code>null</code> se nao existir.
     */
    public String getValue(final String key) {
        
        return map.get(key);
         
    }//getValue
    
    /**
     * Indica se ha alguma entrada (par chave/valor) no arquivo de configuraçao.
     * 
     * @return <code>true</code> se existir algum par chave valor no arquivo. <false> se o arquivo estiver vazio ou nao existir.
     */
    public boolean thereAreEntries() {
        
        return !map.isEmpty();
        
    }//thereAreEntries
    
    /**
     * Retorna todas as chaves em um conjunto.
     * 
     * @return Todas as chaves em um <code>Set</code>.
     */
    public Set<String> keySet() {
        
        return map.keySet();
        
    }//keySet()
    
    /**
     * 
     */
    public static void main(String[] args) throws java.io.IOException {
        
        Config config = new Config("config.cfg");
        
        config.read();
        
        config.put("chaveExtra", "valor");
        
        Set<String> set = config.keySet();
        
        for (String key : set) {
            
            System.out.println(key + " : " + config.map.get(key));
            
        }
        
        config.write();
        
    }
    
}//classe Config
