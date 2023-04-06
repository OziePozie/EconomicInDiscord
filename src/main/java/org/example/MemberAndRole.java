// 
// Decompiled by Procyon v0.5.36
// 

package org.example;

public class MemberAndRole
{
    String memberid;
    String roleID;
    
    public MemberAndRole(final String memberid, final String roleID) {
        this.memberid = memberid;
        this.roleID = roleID;
    }
    
    public String getMemberid() {
        return this.memberid;
    }
    
    public String getRoleID() {
        return this.roleID;
    }
    
    public void setMemberid(final String memberid) {
        this.memberid = memberid;
    }
    
    public void setRoleID(final String roleID) {
        this.roleID = roleID;
    }
    
//    @Override
//    public String toString() {
//        return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;, this.memberid, this.roleID);
//    }
}
