class ConfirmedGroups < ActiveRecord::Base
    #member is a string
    def add_member(member)
        current_members = self.members
        if not self.members.include? member
            self.update(members: current_members + "," + member)
        end
    end
end
