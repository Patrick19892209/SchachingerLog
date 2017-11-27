package view;
import controller.ReklaData;

public class Rekla {
	
	private String id;
	private String aviso;
	private String erfasser;
	private String artNr;
	private String an;
	private String menge;
	private String mangel;
	private ReklaData redat;
	
	public Rekla() {
	}

	public boolean insert() {
		if (this.erfasser!=null&&this.artNr!=null&&this.mangel!=null&&this.menge!=null&&this.mangel!=null) {
			ReklaData redat = new ReklaData(this.erfasser, this.artNr, this.an, this.menge, this.mangel);
			redat.insertRekla();
			return true;
		}
		else {
			return false;
		}
		
	}
	
	public ReklaData getRedat() {
		return redat;
	}

	public void setRedat(ReklaData redat) {
		this.redat = redat;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAviso() {
		return aviso;
	}

	public void setAviso(String aviso) {
		this.aviso = aviso;
	}

	public String getActorA() {
		return erfasser;
	}

	public void setActorA(String erfasser) {
		this.erfasser = erfasser;
	}

	public String getActorB() {
		return an;
	}

	public void setActorB(String an) {
		this.an = an;
	}

	public String getArtNr() {
		return artNr;
	}

	public void setArtNr(String artNr) {
		this.artNr = artNr;
	}

	public String getAn() {
		return an;
	}

	public void setAn(String an) {
		this.an = an;
	}

	public String getMenge() {
		return menge;
	}

	public void setMenge(String menge) {
		this.menge = menge;
	}

	public String getMangel() {
		return mangel;
	}

	public void setMangel(String mangel) {
		this.mangel = mangel;
	};
	
	
	
}
