/*
 *  This file is part of AndroidAppStudio.
 *
 *  AndroidAppStudio is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidAppStudio is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidAppStudio.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.icst.android.appstudio.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.icst.android.appstudio.beans.utils.BeansUIDConstants;
import com.icst.android.appstudio.beans.utils.CodeFormatterUtils;

public class VariableBean implements CloneableBean<VariableBean>, Serializable {
	public static final long serialVersionUID = BeansUIDConstants.VARIABLE_BEAN;

	public static final int ACCESS_MODIFIER_PRIVATE = 1;
	public static final int ACCESS_MODIFIER_PROTECTED = 2;
	public static final int ACCESS_MODIFIER_PUBLIC = 3;

	public static final int VARIABLE = 0;
	public static final int LAYOUT = 1;

	private int accessModifier;
	private String variableTitle;
	private String variableType;
	private String variableName;
	private String nonFixedVariableName;
	private String variableInitializerCode;
	private boolean mustBeGloballyIntialized;
	private boolean isInitializedGlobally;
	private boolean canInitializedGlobally;
	private boolean isStaticVariable;
	private boolean isFinalVariable;
	private boolean applyColorFilter;
	private byte[] icon;
	private VariableBean[] requiredVariables;
	private HashMap<String, String> variableTitles;
	private HashMap<String, String> variableValues;
	private HashMap<String, Integer> inputType;

	@Override
	public VariableBean cloneBean() {
		VariableBean variable = new VariableBean();
		variable.accessModifier = new Integer(this.accessModifier);
		variable.variableTitle = this.variableTitle == null ? null : new String(this.variableTitle);
		variable.variableType = this.variableType == null ? null : new String(this.variableType);
		variable.variableName = this.variableName == null ? null : new String(this.variableName);
		variable.mustBeGloballyIntialized = new Boolean(this.mustBeGloballyIntialized);
		variable.isInitializedGlobally = new Boolean(this.isInitializedGlobally);
		variable.canInitializedGlobally = new Boolean(this.canInitializedGlobally);
		variable.isStaticVariable = new Boolean(this.isStaticVariable);
		variable.isFinalVariable = new Boolean(this.isFinalVariable);
		variable.applyColorFilter = new Boolean(this.applyColorFilter);
		variable.nonFixedVariableName = this.nonFixedVariableName == null ? null
				: new String(this.nonFixedVariableName);
		variable.variableInitializerCode = this.variableInitializerCode == null ? null
				: new String(this.variableInitializerCode);

		if (this.icon == null) {
			variable.icon = null;
		} else {
			variable.icon = new byte[this.icon.length];
			System.arraycopy(this.icon, 0, variable.icon, 0, this.icon.length);
		}

		if (this.requiredVariables == null) {
			variable.requiredVariables = null;
		} else {
			variable.requiredVariables = new VariableBean[this.requiredVariables.length];
			for (int i = 0; i < this.requiredVariables.length; i++) {
				variable.requiredVariables[i] = this.requiredVariables[i] == null ? null
						: this.requiredVariables[i].cloneBean();
			}
		}

		if (this.variableTitles == null) {
			variable.variableTitles = null;
		} else {
			variable.variableTitles = new HashMap<>(this.variableTitles);
		}

		if (this.variableValues == null) {
			variable.variableValues = null;
		} else {
			variable.variableValues = new HashMap<>(this.variableValues);
		}

		if (this.inputType == null) {
			variable.inputType = null;
		} else {
			variable.inputType = new HashMap<>(this.inputType);
		}

		return variable;
	}

	public String getDefCode() {
		StringBuilder code = new StringBuilder();

		if (getRequiredVariables() != null) {
			for (int i = 0; i < getRequiredVariables().length; ++i) {
				if (i != 0) {
					code.append("\n");
				}

				VariableBean variable = getRequiredVariables()[i];
				code.append(getDefCode(getVariableValues(), this));
			}

			if (getRequiredVariables().length > 0) {
				code.append("\n");
			}

			if (variableType == null) {
				return code.toString();
			}
		}

		String accessModifier = switch (getAccessModifier()) {
			case ACCESS_MODIFIER_PRIVATE -> "private";
			case ACCESS_MODIFIER_PROTECTED -> "protected";
			case ACCESS_MODIFIER_PUBLIC -> "public";
			default -> "";
		};

		code.append(accessModifier);
		code.append(" ");

		if (isStaticVariable) {
			code.append("static");
			code.append(" ");
		}

		if (isFinalVariable) {
			code.append("final");
			code.append(" ");
		}

		code.append(variableType);
		code.append(" ");
		if (getNonFixedVariableName() == null) {
			code.append(variableName);
		} else {
			code.append(
					getNonFixedVariableName()
							.replace(CodeFormatterUtils.getKeySyntaxString("variableName"), variableName));
		}

		if (isInitializedGlobally) {
			if (getVariableInitializerCode() == null) {
				code.append(";");
			} else {
				code.append(" = ");

				String init = new String(getVariableInitializerCode());

				for (Map.Entry<String, String> entry : variableValues.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					init = init.replace(CodeFormatterUtils.getKeySyntaxString(key), value);
				}

				code.append(init);
			}
			code.append(";");
		} else {
			code.append(";");
		}

		return code.toString();
	}

	private String getDefCode(
			HashMap<String, String> variableValues, VariableBean parentVariableModel) {
		StringBuilder code = new StringBuilder();

		String accessModifier = switch (getAccessModifier()) {
			case ACCESS_MODIFIER_PRIVATE -> "private";
			case ACCESS_MODIFIER_PROTECTED -> "protected";
			case ACCESS_MODIFIER_PUBLIC -> "public";
			default -> "";
		};

		code.append(accessModifier);
		code.append(" ");

		if (isStaticVariable) {
			code.append("static");
			code.append(" ");
		}

		if (isFinalVariable) {
			code.append("final");
			code.append(" ");
		}

		code.append(variableType);
		code.append(" ");
		if (getNonFixedVariableName() == null) {
			code.append(variableName);
		} else {
			code.append(
					getNonFixedVariableName()
							.replace(CodeFormatterUtils.getKeySyntaxString("variableName"), variableName));
		}

		if (isInitializedGlobally) {
			if (getVariableInitializerCode() == null) {
				code.append(";");
			} else {
				code.append(" = ");

				String init = new String(getVariableInitializerCode());

				for (Map.Entry<String, String> entry : variableValues.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					init = init.replace(CodeFormatterUtils.getKeySyntaxString("variable"), value);
				}

				code.append(init);
			}
			code.append(";");
		} else {
			code.append(";");
		}

		return code.toString();
	}

	public int getAccessModifier() {
		return this.accessModifier;
	}

	public void setAccessModifier(int accessModifier) {
		this.accessModifier = accessModifier;
	}

	public String getVariableTitle() {
		return this.variableTitle;
	}

	public void setVariableTitle(String variableTitle) {
		this.variableTitle = variableTitle;
	}

	public String getVariableType() {
		return this.variableType;
	}

	public void setVariableType(String variableType) {
		this.variableType = variableType;
	}

	public String getVariableName() {
		return this.variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getNonFixedVariableName() {
		return this.nonFixedVariableName;
	}

	public void setNonFixedVariableName(String nonFixedVariableName) {
		this.nonFixedVariableName = nonFixedVariableName;
	}

	public String getVariableInitializerCode() {
		return this.variableInitializerCode;
	}

	public void setVariableInitializerCode(String variableInitializerCode) {
		this.variableInitializerCode = variableInitializerCode;
	}

	public boolean getMustBeGloballyIntialized() {
		return this.mustBeGloballyIntialized;
	}

	public void setMustBeGloballyIntialized(boolean mustBeGloballyIntialized) {
		this.mustBeGloballyIntialized = mustBeGloballyIntialized;
	}

	public boolean getIsInitializedGlobally() {
		return this.isInitializedGlobally;
	}

	public void setIsInitializedGlobally(boolean isInitializedGlobally) {
		this.isInitializedGlobally = isInitializedGlobally;
	}

	public boolean getCanInitializedGlobally() {
		return this.canInitializedGlobally;
	}

	public void setCanInitializedGlobally(boolean canInitializedGlobally) {
		this.canInitializedGlobally = canInitializedGlobally;
	}

	public boolean getIsStaticVariable() {
		return this.isStaticVariable;
	}

	public void setIsStaticVariable(boolean isStaticVariable) {
		this.isStaticVariable = isStaticVariable;
	}

	public boolean getIsFinalVariable() {
		return this.isFinalVariable;
	}

	public void setIsFinalVariable(boolean isFinalVariable) {
		this.isFinalVariable = isFinalVariable;
	}

	public byte[] getIcon() {
		return this.icon;
	}

	public void setIcon(byte[] icon) {
		this.icon = icon;
	}

	public VariableBean[] getRequiredVariables() {
		return this.requiredVariables;
	}

	public void setRequiredVariables(VariableBean[] requiredVariables) {
		this.requiredVariables = requiredVariables;
	}

	public HashMap<String, String> getVariableTitles() {
		return this.variableTitles;
	}

	public void setVariableTitles(HashMap<String, String> variableTitles) {
		this.variableTitles = variableTitles;
	}

	public HashMap<String, String> getVariableValues() {
		return this.variableValues;
	}

	public void setVariableValues(HashMap<String, String> variableValues) {
		this.variableValues = variableValues;
	}

	public HashMap<String, Integer> getInputType() {
		return this.inputType;
	}

	public void setInputType(HashMap<String, Integer> inputType) {
		this.inputType = inputType;
	}

	public boolean getApplyColorFilter() {
		return this.applyColorFilter;
	}

	public void setApplyColorFilter(boolean applyColorFilter) {
		this.applyColorFilter = applyColorFilter;
	}
}
