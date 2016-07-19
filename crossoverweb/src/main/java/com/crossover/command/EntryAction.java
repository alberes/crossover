package com.crossover.command;

import org.apache.log4j.Logger;

public class EntryAction extends ActionCommand {
	
	Logger logger = Logger.getLogger(EntryAction.class);

	@Override
	public void execute() {
		logger.info("New access.");
		this.forward("/index.jsp");
	}

}
