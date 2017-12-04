package model;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import controller.ReklaData;

@FacesValidator("model.AbbrevationValidator")
public class AbbrevationValidator implements Validator {
	@Override
	public void validate(FacesContext facesContext, UIComponent component, Object value) throws ValidatorException {
		if (value!=null) {
			ReklaData rd = new ReklaData();
			if(!rd.abbrevationExists(value.toString())) {
				FacesMessage message = new FacesMessage(value.toString() + " existiert nicht!");
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(message);
			}
		}
	}
}