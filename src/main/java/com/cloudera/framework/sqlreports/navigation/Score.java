package com.cloudera.framework.sqlreports.navigation;

import com.cloudera.framework.sqlreports.SessionContext;
import com.cloudera.framework.sqlreports.definition.Item;
import com.cloudera.framework.sqlreports.definition.Query;
import com.cloudera.framework.sqlreports.definition.QueryGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Score implements NavigationSelection, Description {
    private static final Logger LOG = LogManager.getLogger(Score.class);

    private String shortDesc = null;
    private String longDesc = null;

    private List<WeightedOption> scored = new ArrayList<WeightedOption>();

    @JsonProperty(value = "scoring_key_field")
    private String scoringKeyField = null;
    private List<String> display = new ArrayList<String>();

    private List<Scenario> scenarios = new ArrayList<Scenario>();

    @JsonProperty(value = "detail_references")
    private List<String> detailReferences = null;
    @JsonProperty(value = "detail_groups")
    private List<String> detailGroups = null;

    @JsonIgnore
    private List<Item> details = new ArrayList<Item>();
    @JsonIgnore
    private Boolean resolvedReferences = Boolean.FALSE;

    public Boolean hasDetails() {
        if (details.size() > 0) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public void loadResources() {
        for (WeightedOption option : getScored()) {
            try {
                option.getQueryItem();
            } catch (RuntimeException rte) {
                LOG.error(rte);
                rte.printStackTrace();
            }
        }
        resolveReferences();
    }

    public List<WeightedOption> getScored() {
        return scored;
    }

    public void setScored(List<WeightedOption> scored) {
        this.scored = scored;
    }

    public String getScoringKeyField() {
        return scoringKeyField;
    }

    public void setScoringKeyField(String scoringKeyField) {
        this.scoringKeyField = scoringKeyField;
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<Scenario> scenarios) {
        this.scenarios = scenarios;
    }

    public List<String> getDetailReferences() {
        return detailReferences;
    }

    public void setDetailReferences(List<String> detailReferences) {
        this.detailReferences = detailReferences;
    }

    public List<String> getDetailGroups() {
        return detailGroups;
    }

    public void setDetailGroups(List<String> detailGroups) {
        this.detailGroups = detailGroups;
    }

    public List<Item> getDetails() {
        return details;
    }

    public void setDetails(List<Item> details) {
        this.details = details;
    }

    public List<String> getDisplay() {
        return display;
    }

    public void setDisplay(List<String> display) {
        this.display = display;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    @Override
    public String getShortDesc() {
        return this.shortDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    @Override
    public String getLongDesc() {
        return this.longDesc;
    }

    @Override
    public String getName() {
        return getShortDesc();
    }

    @Override
    public String getType() {
        return "Score";
    }

    protected Boolean resolveAndAddDefinition(String definitionReference) {
        Boolean rtn = Boolean.TRUE;
        Item referencedItem = SessionContext.getInstance().getQueryResource().getDefinitions().get(definitionReference);
//        Item referencedItem = definitions.get(definitionReference);
        if (referencedItem != null) {
            Item clonedReferenceItem = null;
            try {
                clonedReferenceItem = referencedItem.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            details.add(clonedReferenceItem);
        } else {
            LOG.error("Definition: " + definitionReference + " not found.  Fix definition.");
            rtn = Boolean.FALSE;
        }
        return rtn;
    }

    /*
        There are object in the instance that are references to other objects.  This is designed to create
        copies of those references and associate them where needed.

        Should be called BEFORE loadResources.
         */
    protected Boolean resolveReferences() {
        Boolean rtn = Boolean.TRUE;
        if (!resolvedReferences) {
            // If there are references AND they haven't already been loaded.
            if (this.getDetailReferences() != null && this.getDetails().size() == 0) {
                for (String itemReference : this.getDetailReferences()) {
                    Boolean addCheck = resolveAndAddDefinition(itemReference);
                    if (!addCheck)
                        rtn = addCheck;
                }
            }
            if (this.getDetailGroups() != null) {
                for (String detailGroupKey : this.getDetailGroups()) {
                    // Find this group in the queryresource.
                    QueryGroup queryGroup = SessionContext.getInstance().getQueryResource().getGroups().get(detailGroupKey);
                    for (String itemReference : queryGroup.getDetailReferences()) {
                        Boolean addCheck = resolveAndAddDefinition(itemReference);
                        if (!addCheck)
                            rtn = addCheck;
                    }
                }
            }
            resolvedReferences = Boolean.TRUE;
        }
        return rtn;
    }

}
