package toolbox.textfile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import toolbox.regex.Regex;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.lang.IllegalStateException;
import java.lang.IndexOutOfBoundsException;

/*******************************************************************************
* Uma classe para leitura e edição de arquivos tipo texto de até 2GB.
*
* <p>Um objeto desta classe pode carregar o conteudo de um arquivo texto
* inteiramente na memória, em uma única operação atômica, e realizar todas as
* edições necessárias nesta cópia do arquivo em memória.</p>
*
* <p>Este conteudo editado pode ser gravado de volta em disco:</p>
* <ul>
* <li>{@link #write() Sobrescrevendo o arquivo lido}</li>
* <li>{@link #write(String) Em um novo arquivo}</li>
* <li>{@link #writeWithExtPrefix(String) Arquivo com mesmo nome, mas
*     adicionando um prefixo a extensao do arquivo lido}</li>
* </ul>
*
* <p>Embora o objetivo principal do projeto da classe seja fornecer um meio
* conveniente para leitura e edicao de um arquivo texto, a classe fornece
* varios outros metodos uteis no processamento de arquivos tipo texto.</p>
*
* @since 1.0 - 14 de janeiro de 2024
* @version 1.0
* @author Pedro Reis
*******************************************************************************/
 
public class TextFileHandler { 
    
    //Caminho e nome do arquivo na forma como foi passado ao construtor
    private final String pathname;
 
    //Armazena o conteudo do arquivo que foi lido ou o que foi passado
    //ao metodo setContent()
    private String content;
 
    //O encoding usado para ler e gravar o arquivo
    private Charset charset;
 
    //Objeto Scanner para ler a String content
    private Scanner scanner;
 
    //O caminho absoluto do arquivo sem o nome do arquivo
    private final String absolutePath;
 
    //O caminho relativo ao diretorio corrente sem o nome 
    //do arquivo. Se o diretorio corrente nao fizer parte
    //do pathname passado ao construtor, esse campo 
    //sera igual ao caminho absoluto
    private final String relativePath;
 
    //O nome do arquivo com sua extensao
    private final String filename;
 
    //A extensao do arquivo sem o ponto
    private final String extension;
 
    //Um mapa que associa blocos do arquivo que nao podem ser editados
    //a marcadores indexados
    private Map<String, String> lock;
    
    //Objeto Matcher para localizar regex para o metodo hasNextPattern
    private Matcher matcher;
    
    /***************************************************************************
    * Obtém somente o caminho do arquivo ou diretorio passado no argumento
    * <b><i>pathname</i></b>. Sem o nome do arquivo ou diretorio.
    *
    * <p>O método tenta obter o caminho absoluto, mas se não for possível
    * retorna o caminho relativo.</p>
    *
    * <p>O arquivo ou diretorio nao precisa existir no sistema de arquivos.
    * Se o argumento passado for um pathname valido, o caminho do arquivo
    * ou diretorio neste pathname sera retornado.</p>
    *
    * @param pathname Caminho de arquivo ou diretório.
    *
    * @return O caminho absoluto, se possível. Caso contrario, o caminho
    * relativo.
    *
    * @throws IllegalArgumentException Se <b><i>pathname</i></b> for invalido,
    * vazio ou null.
    ***************************************************************************/
    public static String getAbsolutePath(final String pathname) 
        throws IllegalArgumentException {
     
        if (pathname.isEmpty()) 
            throw new IllegalArgumentException("Empty pathname");
     
        String path;
     
        try {
         
            path = new File(new File(pathname).getCanonicalPath()).getParent();
        }
        catch (NullPointerException e) {
         
            throw new IllegalArgumentException("Invalid pathname > " + pathname);
        }
        catch (IOException e) {
         
            path = new File(pathname).getParent();
        }
     
        if (path == null) 
            throw new IllegalArgumentException("Invalid pathname > " + pathname);
     
        return path;
     
    }//getAbsolutePath
    
    /***************************************************************************
    * Obtém somente o caminho do arquivo passado no argumento
    * <b><i>pathname</i></b> do construtor da classe. Sem o nome do arquivo.
    *
    * <p>O método tenta obter o caminho absoluto, mas se não for possível
    * retorna o caminho relativo.</p>
    *
    * @return O caminho absoluto, se possível. Caso constrario, o caminho
    * relativo.
    ***************************************************************************/
    public String getAbsolutePath() {
     
        return absolutePath;
     
    }//getAbsolutePath
    
    /***************************************************************************
    * Obtém somente o caminho relativo ao diretorio corrente do arquivo passado
    * no argumento <b><i>pathname</i></b> do construtor da classe. Sem o nome
    * do arquivo.
    *
    * <p>Se o diretorio corrente nao fizer parte de <b><i>pathname</i></b> sera
    * entao retornado o caminho absoluto.</p>
    ***************************************************************************/
    public String getRelativePath() {
     
        return relativePath;
     
    }//getRelativePath
    
    /***************************************************************************
    * Obtém o nome do arquivo no argumento <b><i>pathname</i></b>. (Extensao
    * inclusa).
    *
    * <p>O mesmo que retornaria o metodo
    * {@link java.io.File#getName() getName} da classe java.io.File.</p>
    *
    * @param pathname O pathname de um arquivo.
    *
    * @return O nome do arquivo. Sem o caminho.
    ***************************************************************************/
    public static String getName(final String pathname) {
     
        return new File(pathname).getName();
     
    }//getName
    
    /***************************************************************************
    * Obtém o nome do arquivo no argumento <b><i>pathname</i></b> passado ao
    * construtor da classe. (Extensao inclusa).
    *
    * <p>O mesmo que retornaria o metodo
    * {@link java.io.File#getName() getName} da classe java.io.File.</p>
    *
    * @return Somente o nome do arquivo. Sem o caminho.
    ***************************************************************************/
    public String getName() {
     
        return filename;
     
    }//getName
    
    /***************************************************************************
    * Obtém a extensão no nome de um arquivo. Sem o ponto.
    *
    * @param pathname O pathname do arquivo.
    *
    * @return A extensão do arquivo sem o ponto. Se o arquivo nao tiver
    * extensao sera retornada uma string vazia.
    ***************************************************************************/
    public static String getExt(final String pathname) {
     
        int p = pathname.lastIndexOf('.');
        if (p < 1) return "";
        return pathname.substring(p + 1, pathname.length());
     
    }//getExt
    
    /***************************************************************************
    * Obtém a extensão do arquivo no argumento <b><i>pathname</i></b> passado
    * ao construtor da classe. Sem o ponto.
    *
    * @return Somente a extensão do arquivo eem o ponto. Se o arquivo nao tiver
    * extensao sera retornada uma string vazia.
    ***************************************************************************/
    public String getExt() {
     
        return extension;
     
    }//getExt
    
    /***************************************************************************
    * Obtém o pathname como foi passado ao construtor da classe.
    *
    * @return O pathname como foi passado ao construtor da classe.
    ***************************************************************************/
    public String getPathname() {
     
        return pathname;
     
    }//getPathname
    
    /***************************************************************************
    * Construtor.
    *
    * @param pathname O pathname do arquivo texto. Não pode exceder 2GB e o
    * construtor nao checa se o arquivo e realmente do tipo texto.
    *
    * @param charsetName O encoding que será usado para ler e gravar o arquivo.
    *
    * <p>Os seguintes parâmetros são aceitos:</p>
    * <ul>
    * <li>iso-8859-1
    * <li>us-ascii
    * <li>utf16
    * <li>utf_16be
    * <li>utf_16le
    * <li>utf8
    * </ul>
    *
    * <p>Alguns aliases dos nomes acima tambem poderao ser aceitos.</p>
    *
    * @see java.nio.charset.Charset
    *
    * @throws IllegalArgumentException Se <b><i>pathname</i></b> for invalido,
    * vazio ou null. Ou <b><i>charsetName</i></b> for <code>null</code>.
    *
    * @throws IllegalCharsetNameException Se <b><i>charsetName</i></b> nao
    * obedecer as regras para nomear charsets.
    *
    * @throws UnsupportedCharsetException Se <b><i>charsetName</i></b> nao for
    * suportado pela JVM.
    ***************************************************************************/
    public TextFileHandler(final String pathname, final String charsetName) 
        throws IllegalArgumentException, 
        IllegalCharsetNameException,
        UnsupportedCharsetException {
         
        charset = Charset.forName(charsetName);
     
        absolutePath = getAbsolutePath(pathname);
     
        this.pathname = pathname;
        
        String relative;
        
        String current = getAbsolutePath(".");
        
        if (absolutePath.contains(current)) {
            
            relative = absolutePath.replace(current, "");
            
            if (relative.startsWith(File.separator)) 
                relativePath = relative.substring(1);
            else
                relativePath = relative;
            
        }
        else relativePath = absolutePath;
     
        filename = getName(pathname);
     
        extension = getExt(filename);
        
        matcher = null;
     
        content = null;
     
    }//construtor
    
    /***************************************************************************
    * Construtor.
    *
    * @param pathname O pathname do arquivo que será lido e/ou gravado.
    * O arquivo nao deve exceder 2GB e sera decodificao quando lido pelo metodo
    * {@link #read() read} usando o charset default do sistema.
    *
    * <p>Se o conteudo lido e/ou editado for gravado em disco (com algum dos
    * metodos para este fim desta classe), ele sera gravado codificado com o
    * charset default do sistema.</p>
    *
    * <p>O construtor nao checa se o arquivo e realmente do tipo texto.</p>
    *
    * @throws IllegalArgumentException Se <b><i>pathname</i></b> for invalido,
    * vazio ou null.
    ***************************************************************************/
    public TextFileHandler(final String pathname) 
        throws IllegalArgumentException {
     
         this(pathname, Charset.defaultCharset().toString());
      
    }//construtor
    
    /***************************************************************************
    * Carrega um novo conteúdo para o objeto <b><i>TextFileHandler</i></b>,
    * descartando o anterior.
    *
    * @param newContent Novo conteúdo.
    ***************************************************************************/
    public void setContent(final String newContent) {
     
        content = newContent;
        if (content != null) scanner = new Scanner(content);
     
    }//setContent
    
    /***************************************************************************
    * Retorna o conteúdo atual do arquivo que foi lido ou definido pelo metodo
    * {@link #setContent(String) setContent}.
    *
    * @return O conteúdo atual do arquivo lido.
    ***************************************************************************/
    public String getContent() {
     
        return content;
     
    }//getContent
    
    /*-------------------------------------------------------------------------
    * O objetivo deste método é bloquear certos padrões de substrings, para
    * que não sejam localizadas e alterados pelo método edit().
    ***************************************************************************/
    private void lock(final String[] patterns) {

        if (patterns == null) return;
    
        int countLocks = 0;
        
        lock = new HashMap<>();
        
        for (String pattern : patterns) {

            Regex regex = new Regex(pattern);
            
            regex.setTarget(content);
            
            String match;
            
            while ((match = regex.find()) != null) {

                lock.put("\u0ca0\u13c8" + countLocks++ + "\u13da\u0ca0", match);
            }

            for (String key : lock.keySet()) {

                content = content.replace(lock.get(key), key);
            }
        }

    }//lock

    /*-------------------------------------------------------------------------
    * Restaura todos os blocos que foram travados para a ediçao
    ***************************************************************************/
    private void restoreLocks() {
    
        for (String key : lock.keySet()) {
        
            content = content.replace(key, lock.get(key));
        }
        
        lock = null;
    
    }//restoreLocks

    /***************************************************************************
    * Possibilita editar todas as substrings do arquivo que corresponderem ao
    * padrão passado no argumento <b><i>regex</i></b>.
    *
    * @param regex Um objeto Regex construído com uma expressão regular que
    * localize o tipo de padrão a ser editado.
    *
    * @param lockPatterns Um array de <code>Strings</code> onde cada string
    * deve ser uma expressao regular que localize um bloco de texto que nao
    * deva ser editado.
    *
    * <p>NOTA: Contudo, se um destes blocos de texto ocorrer no escopo de um
    * bloco de texto elegivel para edicao, ele se tornara passivel de edicao.
    * </p>
    *
    * @param editor Um objeto de uma classe que estenda
    * {@link TextFileEditor TextFileEditor}
    * e forneça o método para editar propriamente as substrings localizadas por
    * este método.
    ***************************************************************************/
    public void edit(
        final Regex regex,
        final String[] lockPatterns,
        final TextFileEditor editor
    ) {
     
        if (content == null) return;
     
        lock(lockPatterns);
     
        Map<String, String> map = new HashMap<>();  
     
        regex.setTarget(content);
     
        String match;
     
        while ((match = regex.find()) != null) {
         
            String edited = editor.edit(match);
         
            if (edited != null) map.put(match, edited);
        }
     
        for (String key : map.keySet()) {
         
            content = content.replace(key, map.get(key));
        }
     
        if (lockPatterns != null) restoreLocks();
     
        scanner = new Scanner(content);
        
    }//edit
    
    /***************************************************************************
    * Possibilita editar todas as substrings do arquivo que corresponderem ao
    * padrão passado no argumento <b><i>regex</i></b>.
    *
    * @param regex Uma expressão regular que localize o tipo de padrão a ser
    * editado.
    *
    * @param lockPatterns
    *
    * @param editor Um objeto de uma classe que estenda
    * toolbox.textfile.TextFileEditor e forneça o método para editar
    * propriamente as substrings localizadas por este método.
    ***************************************************************************/
    public void edit(
        final String regex,
        final String[] lockPatterns,
        final TextFileEditor editor
    ) {
    
        edit(new Regex(regex), lockPatterns, editor);
    
    }//edit

    /***************************************************************************
    * Lê o arquivo para a memória em uma única e atômica operação.
    *
    * <p>Suporta somente arquivos texto de até 2GB.</p>
    *
    * @throws IOException Em caso de erro de IO.
    ***************************************************************************/
    public void read() throws IOException {
                
        setContent(Files.readString(Path.of(pathname), charset));
     
    }//read
    
    /***************************************************************************
    * Escreve o texto lido pelo metodo {@link #read() read} ou passado pelo
    * metodo {@link #setContent(String) setContent} (tendo sido editado ou nao)
    * no disco, sobrescrevendo o arquivo <b><i>pathname</i></b>, se este já
    * existir.
    *
    * @param pathname O pathname do arquivo que será gravado.
    *
    * @throws IOException Em caso de erro de IO.
    ***************************************************************************/
    public void write(final String pathname) throws IOException {
     
        if (content == null) return;
        
        Files.writeString(
            Path.of(pathname),
            content,
            charset , 
            StandardOpenOption.WRITE,
            StandardOpenOption.TRUNCATE_EXISTING,
            StandardOpenOption.CREATE
        );
     
    }//write
    
    /***************************************************************************
    * Grava o arquivo no disco, sobrescrevendo o arquivo <b><i>pathname</i></b>,
    * se ja existir e que pode ter tido seu conteudo carregado para o objeto
    * pelo metodo {@link #read() read}.
    *
    * @throws IOException Em caso de erro de IO.
    ***************************************************************************/
    public void write() throws IOException {
     
        write(pathname);
     
    }//write
    
    /***************************************************************************
    * Obtém o nome do arquivo que será gravado pelo método
    * {@link #writeWithExtPrefix(java.lang.String) writeWithExtPrefix}, caso o
    * argumento <b><i>extensionPrefix</i></b> seja passado a este.
    *
    * @param extensionPrefix O prefixo.
    *
    * @return O nome do arquivo que seria gravado com esste prefixo usado na
    * extensão.
    ***************************************************************************/
    public String getFilenameWithExtPrefix(final String extensionPrefix) {
     
       String ext = getExt(); 

       return (
           relativePath + 
           File.separatorChar + filename).replace(ext, extensionPrefix + 
           '.' + 
           ext
       );   
    
    }//getFilenameWithExtPrefix
    
    /***************************************************************************
    * Grava o arquivo no disco em outro arquivo diferente do que foi lido, mas
    * com o mesmo nome, porém com um prefixo acrescido à extensão do arquivo
    * original, passado pelo argumento <b><i>pathname</i></b> ao construtor da
    * classe.
    *
    * @param extensionPrefix Prefixo, sem o ponto, que será usado para
    * diferenciar o nome do arquivo que será gravado, do nome do arquivo
    * passado pelo argumento <b><i>pathname</i></b> ao construtor da classe.
    *
    * @throws IOException Em caso de erro de IO.
    ***************************************************************************/
    public void writeWithExtPrefix(final String extensionPrefix)
        throws IOException {
        
        String ext = getExt(); 

        write(pathname.replace(ext, extensionPrefix + '.' + ext));
     
    }//writeWithExtPrefix
    
    /***************************************************************************
    * Informa se existe mais alguma linha ou token a ser lido no arquivo pelos
    * metodos {@link #nextLine() nextLine} e {@link #nextToken() nextToken}.
    *
    * @return <code>true</code> se existir mais algum token ou linha a ser lida.
    * <code>false</code> se não.
    ***************************************************************************/
    public boolean hasNext() {
     
        if (content == null) return false;
     
        boolean hasNext = scanner.hasNext();
     
        if (!hasNext) scanner = new Scanner(content);
     
        return hasNext;
     
    }//hasNext
    
    /***************************************************************************
    * Lê a próxima linha, se houver.
    *
    * @return A próxima linha ainda não lida ou <code>null</code> se não houver.
    ***************************************************************************/
    public String nextLine() {
     
        if (scanner.hasNext()) 
            return scanner.nextLine();
        else {
            scanner = new Scanner(content);
            return null;
        }
     
    }//nextLine
    
    /***************************************************************************
    * Lê o próximo token, se houver.
    *
    * @return O próximo token ainda não lido ou <code>null</code> se não houver.
    ***************************************************************************/
    public String nextToken() {
    
        if (scanner.hasNext())
        
            return scanner.next();
            
        else {
            
            scanner = new Scanner(content);
            return null;
            
        }
    
    }//nextToken

    /***************************************************************************
    * A representacao textual do objeto.
    *
    * @return Uma representacao textual do objeto.
    ***************************************************************************/
    @Override
    public String toString() {
     
        return String.format("%s :\n\n%s", pathname, content);
     
    }//toString

    /***************************************************************************
    * Define uma regex para pesquisa.
    * 
    * @param pattern A expressao regular para pesquisa.
    *
    * @throws PatternSyntaxException Se a expressao regular for sintaticamente
    * invalida.
    ***************************************************************************/
    public void setPattern(final String pattern) 
        throws PatternSyntaxException {
        
        Pattern p = Pattern.compile(pattern);
        matcher = p.matcher(content);
        
    }//setPattern
    
    /***************************************************************************
    * Retorna <code>true</code> se foi encontrada uma ocorrencia que 
    * corresponde ao padrao definido pelo metodo
    * {@link #setPattern(String) setPattern}.
    * 
    * @return <code>true</code> se foi encontrada uma ocorrencia que 
    * corresponde ao padrao definido pelo metodo
    * {@link #setPattern(String) setPattern}.
    ***************************************************************************/    
    public boolean hasNextPattern() {
        
        return matcher.find();
        
    }//hasNextPattern
    
    /***************************************************************************
    * Retorna uma string correspondendo ao grupo especificado da expressao
    * regular.
    * 
    * @param group O grupo da regex. 0 retorna a correspondencia com a 
    * totalidade da expressao regular.
    *
    * @return O padrao localizado pela ultima chamada do metodo 
    * {@link #hasNextPattern() hasNextPattern} caso a chamada deste metodo
    * tenha retornado <code>true</code>, ou somente o grupo contido neste
    * padrao caso o argumento <b><i>group</i></b> tenha sido passado com valor
    * diferente de 0. Pode retornar uma string vazia.
    * 
    * @throws IllegalStateException Se a ultima chamada de 
    * {@link #hasNextPattern() hasNextPattern} retornou false ou se 
    * {@link #hasNextPattern() hasNextPattern} nao foi chamado previamente.
    * 
    * @throws IndexOutOfBoundsException Se nao existir grupo referente ao 
    * indice passado no argumento <b><i>group</i></b>.
    ***************************************************************************/    
    public String nextPattern(final int group) 
        throws
            IllegalStateException,
            IndexOutOfBoundsException {
        
        return matcher.group(group);
        
    }//nextPattern

}//classe TextFileHandler
