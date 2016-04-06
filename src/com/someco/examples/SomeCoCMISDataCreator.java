package com.someco.examples;

public class SomeCoCMISDataCreator extends CMISExampleBase {

	private static final String USAGE = "java SomeCoDataCreator <username> <password> <root folder> <doc|whitepaper> <content name>";
	private static String[] tab = {"admin","admin","SomeCo","whitepaper","test"}; 
	public static void main(String[] args) throws Exception {
		if (tab.length != 5)
			doUsage(SomeCoCMISDataCreator.USAGE);
		SomeCoCMISDataCreator sccdc = new SomeCoCMISDataCreator();
		sccdc.setUser(tab[0]);
		sccdc.setPassword(tab[1]);
		sccdc.setFolderName(tab[2]);
		sccdc.setContentType(tab[3]);
		sccdc.setContentName(tab[4]);
		sccdc.create();
	}

	public void create() {
		createTestDoc(getContentName(), getContentType());
		return;
	}

}
