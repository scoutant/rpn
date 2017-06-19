package org.scoutant.rpn;

import android.util.Log;

import junit.framework.TestCase;

import orange.com.mykotlin.Calculator;

public class CalculatorTest extends TestCase {

  private Calculator s;

  public void init(){
    s = new Calculator();
  }

  public void testToString() {
    assertTrue( s.isEmpty());
    s.push( 305);
    assertFalse( s.isEmpty());
    assertTrue( s.size()==1);
    Log.d( "calc", "stack is : " + s);
    assertEquals( "305", s.toString());
  }

  private void assertCalc ( Double value) {
    assertEquals(value, s.toString());
  }
  private void assertCalc ( int value) {
    assertCalc( (double)value);
  }

    public void testAdd() {
    init();
    s.push(30.0);
    assertCalc(30);
    s.push(1.15);
    s.add();
    assertCalc(31.15);
  }

  public void testPushAndDrop() {
    s.push(78704);
    s.push(42);
    s.drop();
    assertEquals("Drop failed", "78704.00", s.toString());
    s.drop();
    assertEquals("Double drop failed to empty", "", s.toString());
    s.drop();
    assertEquals("Drop on empty stack failed", "", s.toString());
  }





}
