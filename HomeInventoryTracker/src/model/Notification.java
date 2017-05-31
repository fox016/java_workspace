package model;

import java.util.Objects;

/**
 *
 * @author Talonos
 */
public class Notification 
{
    ObjectType objectType;
    OperationType operationType;
    Object object;
    public Notification(OperationType opt, ObjectType obt, Object o)
    {
        objectType = obt;
        operationType = opt;
        object = o;
    }

    /**
     * Returns the Object this Notification is associated with. Exact use depends
     * on the context of the notification.
     * @return The Object in Question.
     */
    public Object getObject() 
    {
        return object;
    }

    /**
     * Sets the Object associated with this notification. Exact use depends
     * on the context of the notification.
     * @param object The Object to associate with this notification.
     */
    public void setObject(Object object) 
    {
        this.object = object;
    }

    /**
     * Returns the type of object that caused the notification. Note that this
     * is not the same as the type of the object that is bundled in this notification!
     * @return The object type that caused this notification.
     */
    public ObjectType getObjectType() 
    {
        return objectType;
    }

    /**
     * Changes the "Object type" of this notification.
     * @param objectType The new "Object type" of this notification.
     */
    public void setObjectType(ObjectType objectType) 
    {
        this.objectType = objectType;
    }

    /**
     * Returns the operation that caused this notification.
     * @return The operation that caused this notification.
     */
    public OperationType getOperationType() 
    {
        return operationType;
    }

    /**
     * Changes the "Operation Type" of this Notification.
     * @param operationType the new "Operation type" of this notification.
     */
    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    /**
     * Tests for equality, preferable between two Notifications.
     * @param obj the other object.
     * @return True if the other object is not null, a Notification, and has all
     * the same fields. False otherwise.
     */
    @Override
    public boolean equals(Object obj) 
    {
        if (obj == null) 
        {
            return false;
        }
        if (getClass() != obj.getClass()) 
        {
            return false;
        }
        final Notification other = (Notification) obj;
        if (this.objectType != other.objectType) 
        {
            return false;
        }
        if (this.operationType != other.operationType) 
        {
            return false;
        }
        if (!Objects.equals(this.object, other.object)) 
        {
            return false;
        }
        return true;
    }

    /**
     * Provides the hashcode of this notification
     * @return the hash of all fields hashed together.
     */
    @Override
    public int hashCode() 
    {
        int hash = 3;
        hash = 83 * hash + (this.objectType != null ? this.objectType.hashCode() : 0);
        hash = 83 * hash + (this.operationType != null ? this.operationType.hashCode() : 0);
        hash = 83 * hash + Objects.hashCode(this.object);
        return hash;
    }

    
}
