class InvitedGroup < ActiveRecord::Base

    #delete member by email
    def delete_member(member_email)
        #get all members
        invited_members = self.members

        #transform this into a list of members
        string_array_members = invited_members.split(",")
        student_array_members = Array.new

        string_array_members.each do  |member|
            if member != member_email
                student = Student.find_by_email(member)
                student_array_members.push(student)
            end
        end

        #convert back to string
        updated_members = ""
        student_array_members.each do |student|
            updated_members = updated_members + student.email + ","
        end
        #remove last comma
        updated_members.chop!

        #update
        self.update(members: updated_members)
        return self
    end
end
