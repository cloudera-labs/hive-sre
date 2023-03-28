package com.cloudera.framework.sqlreports.navigation;

import com.cloudera.framework.sqlreports.SessionContext;
import com.cloudera.framework.sqlreports.definition.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class WeightedOption implements Description {
    @JsonProperty(value = "definition_reference")
    private String definitionReference = null;
    private int weight = 0;

    @JsonIgnore
    private Item queryItem = null;

    // Need to see how long it takes to generate this report before
    // drill-downs.
    //    @JsonProperty(value = "drill_downs")
//    private List<Option> drillDowns = new ArrayList<Option>();

    public String getShortDesc() {
        if (queryItem != null)
            return queryItem.getShortDesc();
        else
            return "";
    }

    public String getLongDesc() {
        if (queryItem != null)
            return queryItem.getLongDesc();
        else
            return "";
    }

    public String getDefinitionReference() {
        return definitionReference;
    }

    public void setDefinitionReference(String definitionReference) {
        this.definitionReference = definitionReference;
    }

    public Item getQueryItem() {
        if (queryItem == null) {
            queryItem = SessionContext.getInstance().getQueryResource().getDefinitions().get(getDefinitionReference());
            if (queryItem == null) {
                throw new RuntimeException("Couldn't locate query definition reference: " + getDefinitionReference());
            }
        }
        return queryItem;
    }

    public void setQueryItem(Item queryItem) {
        this.queryItem = queryItem;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeightedOption that = (WeightedOption) o;

        if (weight != that.weight) return false;
        return definitionReference.equals(that.definitionReference);
    }

    @Override
    public int hashCode() {
        int result = definitionReference.hashCode();
        result = 31 * result + weight;
        return result;
    }
}
