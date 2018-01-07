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
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import view.Claim;

@ManagedBean
public class FileUploader {
	// private String destination="C:\\temp\\";
	private String path = "C:/Users/Paci/git/SchachingerRep/SchachingerLog/WebContent/resources/images/claims/";
	//private String path="C:/reklaImg/";
	private UploadedFile file;
	private static int index=0;

	@ManagedProperty(value = "#{claim}")
	private Claim claim;

	public void handleFileUpload(FileUploadEvent event) {
		FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, message);
		this.path = this.path + this.claim.getAviso() + "_" + this.claim.getId();

		try {
			copyFile(event.getFile().getFileName(), event.getFile().getInputstream(), this.path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// write the inputStream to a FileOutputStream
	// if directory doesn't exist create directory
	public boolean copyFile(String fileName, InputStream in, String path) {
		boolean ok = false;
		try {
			File newFile = new File(this.path);
			System.out.println(this.path);
			if (!newFile.exists()) {
				System.out.println("creating directory: " + newFile.getName());
				try {
					newFile.mkdir();
					System.out.println("DIR created");
				} catch (SecurityException se) {
					// handle it
				}
			}
			//int index = 0;
			//do {
			index++;
				System.out.println("index: " + index);
				newFile = new File(path + "/" + fileName);	// + "_" + index + ".jpg");
				System.out.println("path: " + path + "/" + fileName);
			//} while (newFile.exists());
			System.out.println(index);
			
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
			ok = true;
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return ok;
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
	
	public UploadedFile getFile() {
		return file;
	}

	public Claim getClaim() {
		return claim;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}
}
