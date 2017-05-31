package model;

public class Notification
{
	private Object object;
	private NotificationType type;
	
	public Notification(Object obj, NotificationType t)
	{
		setObject(obj);
		setType(t);
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public NotificationType getType() {
		return type;
	}

	public void setType(NotificationType type) {
		this.type = type;
	}
	
}
