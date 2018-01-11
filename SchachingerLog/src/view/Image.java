package view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

@ManagedBean
@ViewScoped
public class Image {

	private String path = "/resources/images/claims/";
	ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
	ServletContext servletContext = (ServletContext) context.getContext();
	private List<String> paths;
	private Claim claim;
	
	public List<String> fetchPaths(Claim claim) {
		this.path = servletContext.getRealPath(this.path);
		String path = this.path + claim.getAviso() + "_" + claim.getId() + "/";

		System.out.println("Claim: " + claim.getAviso() + " \npath: " + path);
		List<String> list = new ArrayList<>();
	    String basePath = new File("").getAbsolutePath();
	    System.out.println("basepath: " + basePath);
		File folder = new File(path);
		System.out.println("Folder path: " + folder.getPath());
		File[] listOfFiles = folder.listFiles();
		if(listOfFiles==null||listOfFiles.length < 1) {
			list.add("emptyBox - keine Bilder gefunden.jpg");
			return list;
		}
		else {
			for (File file : listOfFiles) {
				if (file.isFile()) {
					System.out.println(file.getName());

					list.add(claim.getAviso() + "_" + claim.getId() + "/" + file.getName());
				}
			}
		}
		this.paths=list;
		System.out.println(list.size());
		return list;
	}

	public String getPath() {
		return path;
	}

	public List<String> getPaths() {
		return paths;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setPaths(List<String> paths) {
		this.paths = paths;
	}

	public Claim getClaim() {
		return claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}
}
