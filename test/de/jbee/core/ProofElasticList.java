package de.jbee.core;

public class ProofElasticList {

	public void proofAppendOverride() {
		IElasticList<Integer> l = ElasticList.<Integer> emptyForAppending().append( 1 );
	}

	public void proofDeleteOverride() {
		IElasticList<Integer> l = newIntList().delete( 0 );
	}

	public void proofDropLOverride() {
		IElasticList<Integer> l = newIntList().dropL( 2 );
	}

	public void proofDropROverride() {
		IElasticList<Integer> l = newIntList().dropR( 2 );
	}

	public void proofInsertOverride() {
		IElasticList<Integer> l = newIntList().insert( 2, 1 );
	}

	public void proofPrepandOverride() {
		IElasticList<Integer> l = newIntList().prepand( 1 );
	}

	public void proofTakeLOverride() {
		IElasticList<Integer> l = newIntList().takeL( 2 );
	}

	public void proofTakeROverride() {
		IElasticList<Integer> l = newIntList().takeR( 2 );
	}

	private IElasticList<Integer> newIntList() {
		return ElasticList.<Integer> emptyForPrepanding();
	}
}
