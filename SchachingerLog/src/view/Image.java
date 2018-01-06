package view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

@ManagedBean
public class Image {

	private String path;
	private List<String> paths;
	
	@PostConstruct
	public void init() {
		List<String> list = new ArrayList<>();
		list.add("ausbuchen.jpg");
		list.add("barcode.jpg");
	}

	public void fetchPaths(Claim claim) {
		String path = "/images/claims/";
		System.out.println("Claim: " + claim.getAviso() + " \npath: " + path);
		List<String> list = new ArrayList<>();
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		if (listOfFiles.length > 0) {
			for (File file : listOfFiles) {
				if (file.isFile()) {
					System.out.println(file.getName());

					list.add(file.getName());
				}

			}
		}
		System.out.println(list.size());
	}

	public String getPath() {
		return path;
	}

	public List<String> getPaths() {
		//System.out.println(this.paths.size());
		return paths;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setPaths(List<String> paths) {
		this.paths = paths;
	}
}
