/*
 * Copyright (c) 2022-2023. Cloudera, Inc. All Rights Reserved
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.cloudera.framework.sqlreports.definition;

import com.cloudera.framework.sqlreports.navigation.Description;
import com.cloudera.framework.sqlreports.navigation.NavigationSelection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Item implements NavigationSelection, Description, Cloneable {
    public String getType() {
        return "Item";
    }
    public String getName() {
        return this.shortDesc;
    }

    private String shortDesc = null;
    private String longDesc = null;
    private Query query;
    private List<String> display = new ArrayList<String>();

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public List<String> getDisplay() {
        return display;
    }

    public void setDisplay(List<String> display) {
        this.display = display;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (!Objects.equals(shortDesc, item.shortDesc)) return false;
        if (!Objects.equals(longDesc, item.longDesc)) return false;
        return Objects.equals(query, item.query);
    }

    @Override
    public int hashCode() {
        int result = shortDesc != null ? shortDesc.hashCode() : 0;
        result = 31 * result + (longDesc != null ? longDesc.hashCode() : 0);
        result = 31 * result + (query != null ? query.hashCode() : 0);
        return result;
    }

    @Override
    public Item clone() throws CloneNotSupportedException {
        Item clonedItem = (Item)super.clone();
        clonedItem.setShortDesc(this.shortDesc);
        clonedItem.setLongDesc(this.longDesc);
//        if (this.details != null)
//            clonedItem.setDetails(new ArrayList<String>(this.details));

        return clonedItem;
    }
}
