package com.penglecode.xmodule.myexample.beanvalidation.consts;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

import com.penglecode.xmodule.myexample.beanvalidation.consts.ValidationGroups.ConstraintOrder.Constraint1;
import com.penglecode.xmodule.myexample.beanvalidation.consts.ValidationGroups.ConstraintOrder.Constraint10;
import com.penglecode.xmodule.myexample.beanvalidation.consts.ValidationGroups.ConstraintOrder.Constraint2;
import com.penglecode.xmodule.myexample.beanvalidation.consts.ValidationGroups.ConstraintOrder.Constraint3;
import com.penglecode.xmodule.myexample.beanvalidation.consts.ValidationGroups.ConstraintOrder.Constraint4;
import com.penglecode.xmodule.myexample.beanvalidation.consts.ValidationGroups.ConstraintOrder.Constraint5;
import com.penglecode.xmodule.myexample.beanvalidation.consts.ValidationGroups.ConstraintOrder.Constraint6;
import com.penglecode.xmodule.myexample.beanvalidation.consts.ValidationGroups.ConstraintOrder.Constraint7;
import com.penglecode.xmodule.myexample.beanvalidation.consts.ValidationGroups.ConstraintOrder.Constraint8;
import com.penglecode.xmodule.myexample.beanvalidation.consts.ValidationGroups.ConstraintOrder.Constraint9;

public class ValidationGroups {

	public interface Create {}
	
	public interface Update {}

	public interface Delete {}
	
	public interface Remove {}
	
	public interface Enable {}
	
	public interface Disable {}
	
	public interface UpdatePwd {}
	
	public interface ResetPwd {}

	public interface UpdateState {}
	
	public interface UpdateFlag {}
	
	public interface UpdateType {}
	
	public interface TurnOn {}
	
	public interface TurnOff {}
	
	public interface Online {}
	
	public interface Offline {}
	
	public interface Reset {}
	
	@GroupSequence({Default.class, Constraint1.class, Constraint2.class, Constraint3.class, Constraint4.class, Constraint5.class, Constraint6.class, Constraint7.class, Constraint8.class, Constraint9.class, Constraint10.class})
	public interface ConstraintOrder {
		
		public interface Constraint1 {}
		
		public interface Constraint2 {}

		public interface Constraint3 {}
		
		public interface Constraint4 {}
		
		public interface Constraint5 {}
		
		public interface Constraint6 {}
		
		public interface Constraint7 {}
		
		public interface Constraint8 {}
		
		public interface Constraint9 {}
		
		public interface Constraint10 {}
		
	}
	
}
