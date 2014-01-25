require 'spec_helper'

describe "Static pages" do

  describe "Welcome page" do

    it "should have the content 'This is the Welcome page!" do
      visit '/'
      expect(page).to have_content('This is the Welcome page!')
    end
  end
end