class User < ActiveRecord::Base
  has_secure_password
  validates_presence_of :password, :on => :create
  validates :password, length: { minimum: 8 }

  def password?
    ! self.password_digest.blank?
  end
end
