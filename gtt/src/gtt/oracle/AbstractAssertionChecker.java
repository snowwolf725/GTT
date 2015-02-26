package gtt.oracle;

import gtt.eventmodel.Assertion;
import gtt.eventmodel.IComponent;

public abstract class AbstractAssertionChecker implements AssertionChecker {

	String actualValue = "";

	public String getActualValue() {
		return actualValue;
	}

	public AbstractAssertionChecker() {
		super();
	}

	@Override
	public boolean checkMultipleAssertions(IComponent comp, Assertion assertion) {
		// 此處為多component assert用 method 待補
		return true;
	}

	@Override
	public boolean checkAcutalObject(Object obj, Assertion assertion) {
		actualValue = OracleUtil.getActualValue(obj, assertion);

		Assertion.CompareOperator op = assertion.getCompareOperator();
		if (op == Assertion.CompareOperator.isNull)
			return (assertion.getValue() == null);
		if (op == Assertion.CompareOperator.isNotNull)
			return (assertion.getValue() != null);
		
		if (assertion.getValue() == null)
			return true;
		
		if (op == Assertion.CompareOperator.EqualTo)
			return assertion.getValue().equals(actualValue);
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
}