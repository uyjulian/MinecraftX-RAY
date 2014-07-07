package com.uyjulian.minecraft.XrayMod;

import com.mumfrey.liteloader.core.runtime.Obf;
import com.mumfrey.liteloader.transformers.event.Event;
import com.mumfrey.liteloader.transformers.event.EventInjectionTransformer;
import com.mumfrey.liteloader.transformers.event.MethodInfo;
import com.mumfrey.liteloader.transformers.event.inject.BeforeReturn;
import com.mumfrey.liteloader.transformers.event.inject.MethodHead;

public class XrayModEventTransformer extends EventInjectionTransformer {
	
	private void addCallback(XrayModObfTable obftable) {
		Event shouldSideBeRendered = Event.getOrCreate("shouldSideBeRendered", true);
		MethodInfo mdSideBeRendered =  new MethodInfo(obftable, XrayModObfTable.shouldSideBeRendered, Boolean.TYPE);
		MethodHead injectionPoint = new MethodHead();
		this.addEvent(shouldSideBeRendered, mdSideBeRendered, injectionPoint);
		shouldSideBeRendered.addListener(new MethodInfo("com.uyjulian.minecraft.XrayMod.LiteModUyjuliansXrayMod", "renderSideProcessing"));
	}

	@Override
	protected void addEvents() {
		System.out.println("Transformer is working correctly!");
		addCallback(XrayModObfTable.Block);
	}

}
