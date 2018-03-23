package bar; 

class Other { 
	public Bar method() {
		Bar foo = new Foo(); 
		return foo; 
	}
	
	public Bar method() {
		return new Bar();
	}
}