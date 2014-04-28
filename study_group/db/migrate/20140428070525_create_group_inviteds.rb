class CreateGroupInviteds < ActiveRecord::Migration
  def change
    create_table :group_inviteds do |t|
      t.string :name
      t.string :members
      t.string :master

      t.timestamps
    end
  end
end
