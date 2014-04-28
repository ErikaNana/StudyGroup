class RenameTable < ActiveRecord::Migration
  def change
    rename_table :group_inviteds, :group_invited
  end
end
