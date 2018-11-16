package com.company.hrm.web.orgunit;

import com.company.hrm.entity.OrgUnit;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.components.TextField;

import javax.inject.Named;

public class OrgUnitEdit extends AbstractEditor<OrgUnit> {

    @Named("fieldGroup.name")
    private TextField nameField;
    @Named("fieldGroup.parent")
    private PickerField parentField;
    @Named("fieldGroup.title")
    private TextField titleField;

    @Override
    protected void postInit() {
        super.postInit();
        nameField.addValueChangeListener(e -> updateTitle());
        parentField.addValueChangeListener(e -> updateTitle());
    }

    private void updateTitle() {
        if (parentField.getValue() != null) {
            titleField.setValue(String.format("%s/%s", ((OrgUnit) parentField.getValue()).getTitle(), nameField.getValue()));
        } else {
            titleField.setValue(nameField.getValue());
        }
    }
}