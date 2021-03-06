/*
 * Copyright 2011 Samuli Penttil�
 *
 * This file is part of Uistelup�iv�kirja.
 * 
 * Uistelup�iv�kirja is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Uistelup�iv�kirja is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with Uistelup�iv�kirja. If not, see http://www.gnu.org/licenses/.
 */

package fi.capeismi.fish.uistelupaivakirja.controller;

import java.util.Comparator;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import fi.capeismi.fish.uistelupaivakirja.model.DuplicateItemException;
import fi.capeismi.fish.uistelupaivakirja.model.FishItem;
import fi.capeismi.fish.uistelupaivakirja.model.LureObject;
import fi.capeismi.fish.uistelupaivakirja.model.ModelFactory;
import fi.capeismi.fish.uistelupaivakirja.model.TripObject;

public class FishCounterPart extends CounterPart {
	
	private Comparator<Object> m_comparator = null;
	private FishItem m_fish = null;
	
	private class AlternativeHandler implements OnDismissListener, OnClickListener{		
		private int m_source;
		public AlternativeHandler(int source)
		{
			m_source = source;
		}
		
		@Override
		public void onClick(View arg0) {
			AddSpinnerItem adder = new AddSpinnerItem(m_activity.getActivity());
			adder.setOnDismissListener(this);
			adder.show();			
		}

		@Override
		public void onDismiss(DialogInterface dialog) {
			try
			{
				switch(m_source)
				{
				case R.id.Species:
					ModelFactory.getModel().getSpinnerItems().addSpecies(dialog.toString());
					m_activity.setSpinners(R.id.Species, 
							ModelFactory.getModel().getSpinnerItems().getSpeciesList(),
							m_comparator);
					break;
				case R.id.Getter:
					ModelFactory.getModel().getSpinnerItems().addGetter(dialog.toString());
					m_activity.setSpinners(R.id.Getter, 
							ModelFactory.getModel().getSpinnerItems().getGetterList(),
							m_comparator);
					break;
				case R.id.Method:
					ModelFactory.getModel().getSpinnerItems().addMethod(dialog.toString());
					m_activity.setSpinners(R.id.Method, 
							ModelFactory.getModel().getSpinnerItems().getMethodList(),
							m_comparator);					
					break;
				}
				
				m_activity.setSpinnerDefaultValue(m_source, dialog.toString());
			}catch(DuplicateItemException e)
			{
				//No worry
			}
			
		}
		
	}
	
	public FishCounterPart(FishItem eventitem, TripObject trip, Event.PrivateConduit activity) {
		super(trip, activity);
		m_fish = eventitem;
		setupFishFields();
		readFishFields();
	}

	
    protected void setupFishFields()
    {   	
		m_comparator = new Comparator<Object>() {
			@Override
			public int compare(Object arg0, Object arg1) {
				return arg0.toString().compareTo(arg1.toString());					
			}			
		};
		
		m_activity.setCheckboxListener(R.id.GroupCB, new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				m_activity.setEnabled(R.id.GroupSize, arg1);
				m_activity.setEnabled(R.id.Length, !arg1);
			}
    		
    	});
    
		m_activity.setButtonListener(R.id.AddSpecies, new AlternativeHandler(R.id.Species));
		m_activity.setButtonListener(R.id.AddGetter, new AlternativeHandler(R.id.Getter));
		m_activity.setButtonListener(R.id.AddMethod, new AlternativeHandler(R.id.Method));
    	m_activity.setSpinners(R.id.Species, 
    			ModelFactory.getModel().getSpinnerItems().getSpeciesList(),
    			m_comparator);
    	
    	m_activity.setSpinners(R.id.Getter, 
    			ModelFactory.getModel().getSpinnerItems().getGetterList(),
    			m_comparator);
    	
    	m_activity.setSpinners(R.id.Method, 
    			ModelFactory.getModel().getSpinnerItems().getMethodList(),
    			m_comparator);
    	
    	m_activity.setSpinners(R.id.Lure,
    			ModelFactory.getModel().getLures().getList(),
    			m_comparator);   
    	    	
    }
    
    public void readFishFields()
    {
    	m_activity.setEditText(R.id.Length, m_fish.getLength());
    	m_activity.setEditText(R.id.Weight, m_fish.getWeight());
    	m_activity.setEditText(R.id.SpotDepth, m_fish.getSpotDepth());
    	m_activity.setEditText(R.id.Depth, m_fish.getTotalDepth());
    	m_activity.setEditText(R.id.TrollingSpeed, m_fish.getTrollingSpeed());
    	m_activity.setEditText(R.id.LureWeight, m_fish.getLineWeight());
    	m_activity.setEditText(R.id.ReleaseWidth, m_fish.getReleaseWidth());    	
    	m_activity.setEditText(R.id.GroupSize, m_fish.getGroupAmount());
    	
    	m_activity.setSpinnerDefaultValue(R.id.Getter, m_fish.getGetter());
    	m_activity.setSpinnerDefaultValue(R.id.Method, m_fish.getMethod());
    	m_activity.setSpinnerDefaultValue(R.id.Species, m_fish.getSpecies());
    	
    	m_activity.setChecked(R.id.GroupCB, m_fish.getIsGroup());
    	m_activity.setChecked(R.id.CrCB, m_fish.getIsCatchNReleased());
    	m_activity.setChecked(R.id.UnderSizeCB, m_fish.getIsUndersize());
    	
    	if(m_fish.getLure() != null)
    		m_activity.setSpinnerDefaultValue(R.id.Lure, m_fish.getLure().toString());
    }
    
    public void writeFishFields()
    {
		m_fish.setLength(m_activity.getEditText(R.id.Length));
		m_fish.setWeight(m_activity.getEditText(R.id.Weight));
		m_fish.setSpotDepth(m_activity.getEditText(R.id.SpotDepth));
		m_fish.setTotalDepth(m_activity.getEditText(R.id.Depth));
		m_fish.setTrollingSpeed(m_activity.getEditText(R.id.TrollingSpeed));
		m_fish.setLineWeight(m_activity.getEditText(R.id.LureWeight));
		m_fish.setReleaseWidth(m_activity.getEditText(R.id.ReleaseWidth));
		m_fish.setGroupAmount(m_activity.getEditText(R.id.GroupSize));
    	m_fish.setIsGroup(m_activity.getChecked(R.id.GroupCB));
    	m_fish.setIsUndersize(m_activity.getChecked(R.id.UnderSizeCB));
    	m_fish.setIsCatchNReleased(m_activity.getChecked(R.id.CrCB));
    	
    	if(m_fish.getIsGroup())
    	{
    		try 
    		{
    			if(new Integer(m_fish.getGroupAmount()).intValue() <= 1)
    			{
    				m_fish.setIsGroup(false);
    				m_fish.setGroupAmount("");
    			}
    		} catch(Exception e)
    		{
    			m_fish.setIsGroup(false);
    			m_fish.setGroupAmount("");
    		}
    	}
		
		Object species = m_activity.getSelectedItem(R.id.Species);
		Object getter = m_activity.getSelectedItem(R.id.Getter);
		Object method = m_activity.getSelectedItem(R.id.Method);
		Object lure = m_activity.getSelectedItem(R.id.Lure);
		if(species != null)	m_fish.setSpecies(species.toString());
		if(getter != null)	m_fish.setGetter(getter.toString());
		if(method != null)	m_fish.setMethod(method.toString());
		if(lure != null)	m_fish.setLure((LureObject) lure);
    }
}
