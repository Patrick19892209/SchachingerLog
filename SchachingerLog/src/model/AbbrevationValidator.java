package model;


import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;


@FacesValidator("model.AbbrevationValidator")
public class AbbrevationValidator implements Validator {
	@Override
	public void validate(FacesContext facesContext, UIComponent component, Object value) throws ValidatorException {
		if (value!=null) {
			ClaimData rd = new ClaimData();
			if(!rd.abbrevationExists(value.toString())) {
				FacesMessage message = new FacesMessage("Das Kurzzeichen '" + value.toString() + "' existiert nicht!");
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(message);
			}
		}
	}
}