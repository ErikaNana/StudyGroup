class FixColumnName < ActiveRecord::Migration
  def change
        rename_column :students, :type, :student_type
  end
end
