import{U as e,y as i,T as d,b as p,F as r}from"./indexhtml-2e511133.js";import{labelProperties as l,helperTextProperties as s,errorMessageProperties as n}from"./vaadin-text-field-e82c445d-98eb6465.js";const c={tagName:"vaadin-radio-group",displayName:"RadioButtonGroup",elements:[{selector:"vaadin-radio-group",displayName:"Group",properties:[e.backgroundColor,e.borderColor,e.borderWidth,e.borderRadius,e.padding]},{selector:"vaadin-radio-group::part(label)",displayName:"Label",properties:l},{selector:"vaadin-radio-group::part(helper-text)",displayName:"Helper text",properties:s},{selector:"vaadin-radio-group::part(error-message)",displayName:"Error message",properties:n},{selector:"vaadin-radio-group vaadin-radio-button",displayName:"Radio buttons",properties:[{propertyName:"--vaadin-radio-button-size",displayName:"Radio button size",defaultValue:"var(--lumo-font-size-l)",editorType:i.range,presets:d.lumoFontSize,icon:"square"}]},{selector:"vaadin-radio-group vaadin-radio-button::part(radio)",displayName:"Radio part",properties:[e.backgroundColor,e.borderColor,e.borderWidth]},{selector:"vaadin-radio-group vaadin-radio-button[checked]::part(radio)",stateAttribute:"checked",stateElementSelector:"vaadin-radio-group vaadin-radio-button",displayName:"Radio part (when checked)",properties:[e.backgroundColor,e.borderColor,e.borderWidth]},{selector:"vaadin-radio-group vaadin-radio-button::part(radio)::after",displayName:"Selection indicator",properties:[{...p.iconColor,propertyName:"border-color"}]},{selector:"vaadin-radio-group vaadin-radio-button label",displayName:"Label",properties:[r.textColor,r.fontSize,r.fontWeight,r.fontStyle]}],setupElement(t){const o=document.createElement("vaadin-radio-button"),a=document.createElement("label");a.textContent="Some label",a.setAttribute("slot","label"),o.append(a),t.append(o)}};export{c as default};
