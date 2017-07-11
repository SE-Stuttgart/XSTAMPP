/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package xstampp.usermanagement.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import xstampp.ui.common.shell.ModalShell;
import xstampp.usermanagement.Messages;
import xstampp.usermanagement.UserSystem;
import xstampp.usermanagement.api.IUser;
import xstampp.usermanagement.roles.User;

/**
 * A {@link ModalShell} which has a content consisting of two entry fileds for username and
 * password.
 * 
 * @author Lukas Balzer - initial implementation
 *
 */
public class LoginShell extends AbstractUserShell {

  private boolean useReadOnlyLogin = false;

  /**
   * Constructs a {@link ModalShell} with <code>Login</code> as title and label for the ok/accept
   * button.
   * 
   * @param userSystem
   *          The user system in which the user wants to login
   * @param hidePassword
   *          whether of not the password input should hide or show characters
   */
  public LoginShell(UserSystem userSystem, boolean hidePassword) {
    super(userSystem, true);
    setTitle(Messages.UserContribution_LoginLabel); //$NON-NLS-1$
    setAcceptLabel(Messages.UserContribution_LoginLabel); //$NON-NLS-1$
    if (userSystem.getExclusiveUser() != null) {
      for (IUser user : getUserSystem().getRegistry()) {
        if (user.getUserId().equals(userSystem.getExclusiveUser())) {
          setSelectedUser(user);
          setUserLabelStyle(SWT.READ_ONLY);
          break;
        }
      }
    }
  }

  @Override
  protected void createCenter(Shell shell) {
    super.createCenter(shell);
    BooleanInput input = new BooleanInput(shell, SWT.CHECK, Messages.LoginShell_ReadOnlyLogin, false);
    input.setSelectionListener(new Listener() {

      @Override
      public void handleEvent(Event event) {
        useReadOnlyLogin = ((Button) event.item).getSelection();
        setEnableUserInput(!useReadOnlyLogin);
      }
    });
  }

  @Override
  protected boolean validate() {
    return super.validate() || useReadOnlyLogin;
  }

  @Override
  protected boolean doAccept() {
    if (useReadOnlyLogin) {
      IUser user = new User();
      setSelectedUser(user);
      setReturnValue(user);
      return true;
    }
    for (IUser user : getUserSystem().getRegistry()) {
      if (user.getUsername().equals(getUsername())) {
        if (user.verifyPassword(getPassword())) {
          setSelectedUser(user);
          setReturnValue(user);
          return true;
        } else {
          break;
        }
      }
    }
    invalidate(Messages.LoginShell_InvalidCredentials);
    return false;
  }
}
