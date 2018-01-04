package model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean
public class FileUploader {
	// private String destination="C:\\temp\\";
	private final String PATH = "C:\\reklaImg\\";
	private UploadedFile file;

	public String getPATH() {
		return PATH;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public void handleFileUpload(FileUploadEvent event) {
		FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, message);
		// Do what you want with the file
		try {
			copyFile(event.getFile().getFileName(), event.getFile().getInputstream(), "C:\\reklaImg\\");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public String loadCSV(FileUploadEvent event) throws Exception {
		try {
			copyFile(event.getFile().getFileName(), event.getFile().getInputstream(), "C:/CSVDateien/");
		} catch (IOException e) {
			e.printStackTrace();
		}

		DBConnector dbc = new DBConnector();
		Connection con = dbc.openConnection();
		String result = "stay";
		try {
            con.setAutoCommit(false);
                        
            Statement stmt = null;
            ResultSet rs = null;
            String columns = null, table = null;
            		columns = "(aviso, delivery_nr,"
                		+ " @date, nr_of_items, supplier"
                		+ " ) "
                		+ "SET date = STR_TO_DATE(@date,'%Y%m%d')";
                	table = "Delivery";
             try {
            	 
            	String loadQuery = "LOAD DATA LOCAL INFILE '" + "C:/CSVDateien/".concat(event.getFile().getFileName()) + 
            			"' INTO TABLE " + table + " FIELDS TERMINATED BY ';'" + 
            			" LINES TERMINATED BY '\n' IGNORE 1 LINES " + columns;
            	stmt = con.createStatement();
            	rs = stmt.executeQuery(loadQuery);
                try {
                    rs.close();
                } catch (Exception ignore) {
                }
                  
            } finally {
                try {
                    stmt.close();
                    con.commit();
                } catch (Exception ignore) {
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error executing transactions ..." + ex.getMessage());
            try {
                con.rollback();
                result = "test";
            } catch (Exception ignore) {
                throw ignore;
            }
            throw ex;
        }
		return result;
    

		
		
	/*	DBConnector dbc = new DBConnector();
		Connection con = dbc.openConnection();
		//FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
		//FacesContext.getCurrentInstance().addMessage(null, message);
		// Do what you want with the file
        con.setAutoCommit(false);
        
        Statement stmt = null;
        ResultSet rs = null;
        String columns = null, table = null;
        		columns = "(aviso, delivery_nr,"
            		+ " @date, nr_of_items, supplier"
            		+ " ) "
            		+ "SET date = STR_TO_DATE(@date,'%Y%m%d')";
            	table = "Delivery";
         try {
        	 
        	String loadQuery = "LOAD DATA LOCAL INFILE '" + "C:/Users/bauer/Downloads/Aviso_METRO.CSV" + 
        			"' INTO TABLE " + table + " FIELDS TERMINATED BY ';'" + 
        			" LINES TERMINATED BY '\n' IGNORE 1 LINES " + columns;
        	stmt = con.createStatement();
        	System.out.println(loadQuery);
        	rs = stmt.executeQuery(loadQuery);
            try {
                rs.close();
            } catch (Exception ignore) {
            }
              
        } finally {
            try {
                stmt.close();
                con.commit();
            } catch (Exception ignore) {
            }
        }
    } catch (SQLException ex) {
        System.out.println("Error executing transactions ..." + ex.getMessage());
        try {
            con.rollback();
            result = false;
        } catch (Exception ignore) {
            throw ignore;
        }
        throw ex;
    }

		
		try {
			
			copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	return "stay";*/
}

	public void copyFile(String fileName, InputStream in, String path) {
		try {

			// write the inputStream to a FileOutputStream
			File newFile = new File(path);
			if (!newFile.exists()) {
				System.out.println("creating directory: " + newFile.getName());
				boolean result = false;
		    try{
		        newFile.mkdir();
		        result = true;
		    } 
		    catch(SecurityException se){
		        //handle it
		    }        
		    if(result) {    
		        System.out.println("DIR created");  
		    }
			}
			newFile = new File(newFile.getPath() + "\\" + fileName);
			OutputStream out = new FileOutputStream(newFile);

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			in.close();
			out.flush();
			out.close();

			System.out.println("New file created!");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
