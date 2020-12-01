package resource;

/**
 * ÑÕÉ«
 * <p>
 * Ë³Ðò£º<br>
 * £±£²£³£´£µ£¶£·£¸£¹<br>
 * ²ÊºÚÀ¶ÂÌ»Ò·Ûºì»ÆË®
 *
 * */
public enum Color {

	COLORFUL("colorful", Resource.colorBall), BLACK("black", Resource.blackBall), BLUE(
			"blue", Resource.blueBall), GREEN("green", Resource.greenBall), GREY(
			"grey", Resource.greyBall), PINK("pink", Resource.pinkBall), RED(
			"red", Resource.redBall), YELLOW("yellow", Resource.yellowBall), AQUA(
			"aqua", Resource.aquaBall);
	private String name;
	private Resource resource;

	private Color() {

	}

	private Color(String name, Resource resource) {
		this.setName(name);
		this.resource = resource;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	@Override
	public String toString() {
		return getName();
	}
}
