/*
 * Copyright (C) 2017 Open Whisper Systems
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.whispersystems.contactdiscovery.mappers;

import io.dropwizard.jersey.errors.ErrorMessage;
import org.whispersystems.contactdiscovery.enclave.NoSuchEnclaveException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NoSuchEnclaveExceptionMapper implements ExceptionMapper<NoSuchEnclaveException> {
  @Override
  public Response toResponse(NoSuchEnclaveException ex) {
    return Response.status(404)
                   .type(MediaType.APPLICATION_JSON_TYPE)
                   .entity(new ErrorMessage(404, ex.toString()))
                   .build();
  }
}
