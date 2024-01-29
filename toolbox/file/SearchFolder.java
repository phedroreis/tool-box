package toolbox.file;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedList;
import java.util.List;

/*******************************************************************************
* Permite obter um array com os arquivos de um diretório (opcionalmente também
* de seus subdiretórios) filtrados por uma expressão regular.
*
* @since 1.0 - 14 de janeiro de 2024
* @version 1.0
* @author Pedro Reis
*******************************************************************************/
public final class SearchFolder {
    
    private final File folder;
    
    private String fileFilterRegex;
    
    private boolean searchDirs;
    
    private List<String> pathnamesList;
    
    private List<File> filesList;
    
    /***************************************************************************
    * Construtor.
    *
    * @param pathname O pathname do diretório que será pesquisado.
    ***************************************************************************/
    public SearchFolder(final String pathname) {
        
        folder = new File(pathname);
    
    }//construtor
    
    /***************************************************************************
    * Obtém um array com os pathnames absolutos dos arquivos cujos nomes
    * corresponderem ao padrão da expressão regular passada no parâmetro
    * regex.
    *
    * @param regex Uma expressão regular para filtrar a pesquisa. A regex
    * verificará correspondência com nome e extensão dos arquivos. Se for
    * passada com valor <code>null</code>, a pesquisa retornará todos os
    * arquivos.
    *
    * @param searchDirs Se true, estenderá a pesquisa aos subdiretórios.
    *
    * @return Um array com os pathnames absolutos dos arquivos.
    ***************************************************************************/
    public String[] getPathnamesList(
        final String regex, 
        final boolean searchDirs
    ) {
        
        fileFilterRegex = regex;
        
        pathnamesList = new LinkedList<>();
        
        this.searchDirs = searchDirs;
        
        search(folder, pathnamesList, false);
        
        return pathnamesList.toArray(String[]::new);
        
    }//getPathnamesList
    
    /***************************************************************************
    * Obtém um array de objetos Files relativos aos arquivos cujos nomes
    * corresponderem ao padrão da expressão regular passada no parâmetro
    * regex.
    *
    * @param regex Uma expressão regular para filtrar a pesquisa. A regex
    * verificará correspondência com nome e extensão dos arquivos.
    *
    * @param searchDirs Se true, estenderá a pesquisa aos subdiretórios.
    *
    * @return Um array de Files correspondendo aos arquivos encontrados.
    ***************************************************************************/
    public File[] getFilesList(final String regex, final boolean searchDirs) {
        
        fileFilterRegex = regex;
        
        filesList = new LinkedList<>();
        
        this.searchDirs = searchDirs;
        
        search(folder, filesList, true);
        
        return filesList.toArray(File[]::new);
        
    }//getFilesList    
    
    /*-------------------------------------------------------------------------
       Retorna todos os pathnames absolutos de arquivos aceitos pelo filtro
    --------------------------------------------------------------------------*/       
    private void search(
        final File folder, 
        final List list, 
        final boolean isFile
    ) {        
        
        File[] fileList = folder.listFiles(new FilesFilter());
        
        if (fileList == null) return;
        
        for (File f : fileList)
            
            if (f.isDirectory()) 
                search(f, list, isFile); 
            else 
                if (isFile) list.add(f); else list.add(f.toString());
       
    }//search   
    
    /***************************************************************************
    * O caminho absoluto do diretório pesquisado.
    *
    * @return O caminho absoluto do diretório pesquisado.
    ***************************************************************************/
    @Override
    public String toString() {
        
        return folder.toString();
        
    }//toString
    
/*=============================================================================
                 Classes privadas da classe CountChars
==============================================================================*/   
    private class FilesFilter implements FileFilter {
        
        @Override
        public boolean accept(File pathname) {
            
            if (pathname.isDirectory()) return searchDirs;
            if (fileFilterRegex == null) return true;            
            return pathname.getName().matches(fileFilterRegex); 
            
        }
        
    }//classe FilesFilter-------------------------------------------------------
    
}//classe SearchFolder
