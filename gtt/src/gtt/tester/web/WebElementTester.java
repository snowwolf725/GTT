package gtt.tester.web;

import gtt.eventmodel.IEvent;
import gtt.eventmodel.web.HtmlButton;
import gtt.eventmodel.web.HtmlElement;
import gtt.eventmodel.web.HtmlForm;
import gtt.eventmodel.web.HtmlInputText;
import gtt.eventmodel.web.HtmlLink;
import gtt.eventmodel.web.HtmlSelect;
import gtt.eventmodel.web.HtmlTextArea;
import gtt.eventmodel.web.HtmlImg;
import gtt.eventmodel.web.HtmlRadio;
import gtt.eventmodel.web.HtmlCheckbox;
import gtt.eventmodel.web.HtmlBaseElement;

public class WebElementTester extends HtmlComponentTester {
	//本來應該拆成數個class，但是會造成class很多，以一個class包辦
	
	// 2010/09/30 edit by loveshoo
	// 新增 radio, checkbox, img 三個tag功能選項
	@Override
	public boolean fireEvent(IEvent info, HtmlBaseElement com) {
		
		int eid = info.getEventId();
		
		//HtmlInputText
		if (com instanceof HtmlInputText) {
			HtmlInputText input = (HtmlInputText)com;
			
			if (eid == WebTesterTag.CLEAR) {
				input.clear();
				return true;
			}
			
			if (eid == WebTesterTag.INPUT) {
				String text = info.getArguments().getValue("InputValue");
				input.sendKeys(text);
				return true;
			}
			
		}
		//HtmlTextArea
		if (com instanceof HtmlTextArea) {
			HtmlTextArea textarea = (HtmlTextArea)com;
			
			if (eid == WebTesterTag.CLEAR) {
				textarea.clear();
				return true;
			}
			
			if (eid == WebTesterTag.CLICK) {
				textarea.click();
				return true;
			}
			
			if (eid == WebTesterTag.INPUT) {
				String text = info.getArguments().getValue("InputValue");
				textarea.sendKeys(text);
				return true;
			}
			
			if (eid == WebTesterTag.SET_SELECTED) {
				try {
					textarea.setSelected();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("This component may not be SET_SELECTED!");
					e.printStackTrace();
				}
				return true;
			}
			
		}
		
		//HtmlForm
		if (com instanceof HtmlForm) {
			HtmlForm form = (HtmlForm)com;
			
			if (eid == WebTesterTag.SUBMIT) {
				try {
					form.submit();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("This component may not be submit!");
					e.printStackTrace();
				}
				return true;
			}
		}
		
		//HtmlLink
		if (com instanceof HtmlLink) {
			HtmlLink link = (HtmlLink)com;
			
			if (eid == WebTesterTag.CLICK) {
				link.click();
				return true;
			}
			
		}
		//HtmlButton
		if (com instanceof HtmlButton) {
			HtmlButton button = (HtmlButton)com;
			
			if (eid == WebTesterTag.CLICK) {
				button.click();
				return true;
			}
			
		}
		
		//HtmlSelect
		if (com instanceof HtmlSelect) {
			HtmlSelect select = (HtmlSelect)com;
			
			if (eid == WebTesterTag.DESELECT_All) {
				select.deselectAll();
				return true;
			}
			
			if (eid == WebTesterTag.DESELECT_BY_INDEX) {
				String index = info.getArguments().getValue("Index");
				select.deselectByIndex(Integer.valueOf(index));
				return true;
			}
			
			if (eid == WebTesterTag.DESELECT_BY_VALUE) {
				String value = info.getArguments().getValue("Value");
				select.deselectByValue(value);
				return true;
			}
			
			if (eid == WebTesterTag.DESELECT_BY_VISIBLE_TEXT) {
				String visibleValue = info.getArguments().getValue("VisibleValue");
				select.deselectByValue(visibleValue);
				return true;
			}
			
			if (eid == WebTesterTag.SELECT_BY_INDEX) {
				String index = info.getArguments().getValue("Index");
				select.selectByIndex(Integer.valueOf(index));
				return true;
			}
			
			if (eid == WebTesterTag.SELECT_BY_VALUE) {
				String value = info.getArguments().getValue("Value");
				select.selectByValue(value);
				return true;
			}
			
			if (eid == WebTesterTag.SELECT_BY_VISIBLE_TEXT) {
				String visibleValue = info.getArguments().getValue("VisibleValue");
				select.selectByVisibleText(visibleValue);
				return true;
			}
			
		}
		
		//HtmlRadio
		if (com instanceof HtmlRadio) {
			HtmlRadio input = (HtmlRadio)com;
			
			if (eid == WebTesterTag.CLEAR) {
				input.clear();
				return true;
			}
			
			if (eid == WebTesterTag.CLICK) {
				input.click();
				return true;
			}
			
			if (eid == WebTesterTag.SET_SELECTED) {
				try {
					input.setSelected();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("This component may not be SET_SELECTED!");
					e.printStackTrace();
				}
				return true;
			}
			
		}
		
		//HtmlCheckbox
		if (com instanceof HtmlCheckbox) {
			HtmlCheckbox input = (HtmlCheckbox)com;
			
			if (eid == WebTesterTag.CLEAR) {
				input.clear();
				return true;
			}
			
			if (eid == WebTesterTag.CLICK) {
				input.click();
				return true;
			}
			
			if (eid == WebTesterTag.SET_SELECTED) {
				try {
					input.setSelected();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("This component may not be SET_SELECTED!");
					e.printStackTrace();
				}
				return true;
			}
			
		}		
		
		//HtmlImg
		if (com instanceof HtmlImg) {
			HtmlImg link = (HtmlImg)com;
			
			if (eid == WebTesterTag.CLICK) {
				link.click();
				return true;
			}
			
		}		
		
		//HtmlElement
		if( com instanceof HtmlElement) {
			HtmlElement ele = (HtmlElement)com;
			
			if (eid == WebTesterTag.CLICK) {
				ele.click();
				return true;
			}
			
			if (eid == WebTesterTag.SET_SELECTED) {
				try {
					ele.setSelected();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("This component may not be SET_SELECTED!");
					e.printStackTrace();
				}
				return true;
			}
		}
		
		System.out.println("error type");
		return false;
	} 
}
