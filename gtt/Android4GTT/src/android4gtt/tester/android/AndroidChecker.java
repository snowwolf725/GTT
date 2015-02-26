package android4gtt.tester.android;

import gtt.eventmodel.Assertion;
import gtt.eventmodel.IComponent;
import gtt.oracle.AbstractAssertionChecker;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.R;
import android.view.View;
import android4gtt.component.ControlComponent;
import android4gtt.component.ScreenComponent;

import com.jayway.android.robotium.solo.Solo;

public class AndroidChecker extends AbstractAssertionChecker {

	private Solo m_solo = null;
	
	private Object m_res = null;
	
	private String actualValue = "";
	
	public AndroidChecker(Solo solo, Object res) {
		m_solo = solo;
		m_res = res;
	}
	
	private void logging(String msg) {
		System.out.println("[AndroidAsserter ]" + msg);
	}
	
	@Override
	public boolean check(IComponent comp, Assertion assertion) {
		int id = 0;
		Object component = null;
		
		if (comp.getType().equals("android4gtt.component.ControlComponent"))
			component = new ControlComponent(comp, m_solo);		
		else if (comp.getType().equals("android4gtt.component.ScreenComponent"))
			component = new ScreenComponent(comp, m_solo);
		else {
			try{
				@SuppressWarnings("unchecked")
				Field field = ((Class<R.id>) m_res).getField(comp.getName());
				id = field.getInt(new R.id());
			} catch (Exception e) {
				logging("Component name isn't integer");
			}
			
			component = m_solo.getView(id);
		
			if (component == null) {
				logging("[" + comp + "] can't be found.");
				return false;
			}
	
			if (!(component instanceof View)) {
				logging("[" + comp + "] isn't a View");
				return false;
			}
		}
		return checkAcutalObject(component, assertion);
	}
	
	@Override
	public boolean checkAcutalObject(Object obj, Assertion assertion) {
		actualValue = getActualValue(obj, assertion);

		Assertion.CompareOperator op = assertion.getCompareOperator();
		if (op == Assertion.CompareOperator.isNull)
			return (assertion.getValue() == null);
		if (op == Assertion.CompareOperator.isNotNull)
			return (assertion.getValue() != null);
		
		if (assertion.getValue() == null)
			return true;
		
		if (op == Assertion.CompareOperator.EqualTo) {System.out.println("[Checker] Actual Value: "+actualValue+" Expected Value: "+assertion.getValue());
			return assertion.getValue().equals(actualValue);}
		if (op == Assertion.CompareOperator.NotEqual)
			return !assertion.getValue().equals(actualValue);
		if (op == Assertion.CompareOperator.GreaterThan)
			return assertion.getValue().compareTo(actualValue) < 0;
		if (op == Assertion.CompareOperator.GreaterThanOrEqual)
			return assertion.getValue().compareTo(actualValue) <= 0;
		if (op == Assertion.CompareOperator.LessThan)
			return assertion.getValue().compareTo(actualValue) > 0;
		if (op == Assertion.CompareOperator.LessThanOrEqual)
			return assertion.getValue().compareTo(actualValue) >= 0;

		System.out.format("[Checker] Actual Value: %s, Expected Value: %s \n",
				actualValue, assertion.getValue());
		return false;
	}

	private String getActualValue(Object comp, Assertion as) {
		try {
			Method invokeMethod = comp.getClass().getMethod(as.getMethodName(),
					as.typeClasses());

			Object value = null;
			Object[] argValues = as.getArguments().values().toArray();
			
			if (argValues.length == 0)
				value = invokeMethod.invoke(comp);
			else {
				value = invokeMethod.invoke(comp, argValues);
				
				
			}
			
			// fix for special characters
			String result = value.toString();
			result = result.replaceAll("\\n", "\\\\n");
			result = result.replaceAll("\\t", "\\\\t");
			return result;
		} catch (Exception e) {
			// no vaule
			return "";
		}
	}
	
	public String getActualValue() {
		return actualValue;
	}
	
}
