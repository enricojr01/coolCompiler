package com.enricojr.coollang.ast.constants;

public class CoolIdentifier extends CoolLiteral {
    private String value;

    public CoolIdentifier(String s) {
        this.value = s;
    }

    public String getValue() {
        return this.value;
    }

    public String getValueString() {
        return this.value.toString();
    }

    public void setValue(String v) {
        this.value = v;
    }

    public String toString() {
        return String.format("<Identifier - %s>", this.value);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CoolIdentifier other = (CoolIdentifier) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }
}
