package utilidades.ec.edu.ups.tesiswsnsic;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import controlador.ec.edu.ups.tesiswsnsic.EmpresaControlador;
import modelo.ec.edu.ups.tesiswsnsic.TipoEmpresa;

@FacesConverter(value = "tipoEmpresaConverter")
public class TipoEmpresaConverter implements Converter {

	@Override
    public Object getAsObject(FacesContext ctx, UIComponent uiComponent, String beerId) {
        ValueExpression vex =
                ctx.getApplication().getExpressionFactory()
                        .createValueExpression(ctx.getELContext(),
                                "#{empresaControlador}", EmpresaControlador.class);

        EmpresaControlador empresaControlador = (EmpresaControlador)vex.getValue(ctx.getELContext());
        return empresaControlador.getLanguageDiff(Integer.valueOf(beerId));
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object tipoEmpresa) {
    		return ((TipoEmpresa)tipoEmpresa).getId()+"";
    }
}
