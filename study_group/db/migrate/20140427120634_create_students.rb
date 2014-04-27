class CreateStudents < ActiveRecord::Migration
  def change
    create_table :students do |t|
      t.string :email
      t.string :name
      t.string :schedule
      t.string :type

      t.timestamps
    end
  end
end
