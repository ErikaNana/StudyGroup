class CreateInvitedGroups < ActiveRecord::Migration
  def change
    create_table :invited_groups do |t|
      t.string :name
      t.string :members
      t.string :master

      t.timestamps
    end
  end
end
