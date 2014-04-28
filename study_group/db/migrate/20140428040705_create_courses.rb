class CreateCourses < ActiveRecord::Migration
  def change
    create_table :courses do |t|
      t.string :name
      t.string :days
      t.string :times

      t.timestamps
    end
  end
end
