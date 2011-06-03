package de.jbee.core;

public class ProofElasticList {

	public void proofPrepandOverride() {
		IElasticList<Integer> l = ElasticList.<Integer> emptyForPrepanding().prepand( 1 );
	}

	public void proofAppendOverride() {
		IElasticList<Integer> l = ElasticList.<Integer> emptyForAppending().append( 1 );
	}
}
